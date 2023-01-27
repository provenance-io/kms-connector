package io.provenance.plugins.vault

import com.google.gson.Gson
import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity
import io.provenance.entity.direct.DirectKeyEntity
import io.provenance.plugins.vault.config.VaultSecret
import io.provenance.scope.encryption.model.DirectKeyRef
import java.security.PrivateKey
import java.security.PublicKey
import java.util.UUID
import kong.unirest.Unirest
import mu.KotlinLogging

class VaultPlugin : Plugin {
    private val log = KotlinLogging.logger { }

    override fun supports(pluginSpec: Any): Boolean =
        when (pluginSpec) {
            is VaultSpec -> true
            else -> false
        }
    
    override fun fetch(pluginSpec: Any): KeyEntity {
        val spec = pluginSpec as VaultSpec

        log.info("Fetching properties and creating configuration for ${spec.originatorUuid}")

        val response = Unirest.get(spec.vaultUrl)
            .header("X-Vault-Token", spec.vaultToken)
            .asJson()

        val secret = Gson().fromJson(response.body.toString(), VaultSecret::class.java)

        if (secret?.data == null) {
            if (!secret.errors.isNullOrEmpty()) {
                throw Error(secret.errors.joinToString(limit = 3))
            }
            throw IllegalArgumentException("Could not find secret for ${spec.originatorUuid}.")
        }

        val secretData = secret.data.data
        
        val signingKeyRef = DirectKeyRef(
            getKey(secretData, "public_signing_key", spec.originatorUuid) as PublicKey,
            getKey(secretData, "private_signing_key", spec.originatorUuid) as PrivateKey
        )

//        val encryptionKeyRef = DirectKeyRef(
//            getKey(secretData, "public_encryption_key", spec.originatorUuid) as PublicKey,
//            getKey(secretData, "private_encryption_key", spec.originatorUuid) as PrivateKey
//        )
        
        return DirectKeyEntity(signingKeyRef, getKey(secretData, "private_signing_key", spec.originatorUuid) as PrivateKey)
    }

    private fun getKey(secretData: Map<String, Any>, keyName: String, originator: UUID): String {
        if (!secretData.containsKey(keyName)) {
            throw IllegalArgumentException("Missing $keyName key for $originator.")
        }

        return secretData[keyName].toString()
    }
}
