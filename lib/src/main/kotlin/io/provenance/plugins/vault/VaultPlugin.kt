package io.provenance.plugins.vault

import com.google.gson.Gson
import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.entity.direct.DirectKeyEntity
import io.provenance.plugins.vault.config.VaultSecret
import io.provenance.scope.encryption.model.DirectKeyRef
import io.provenance.scope.encryption.util.toJavaPrivateKey
import io.provenance.scope.encryption.util.toJavaPublicKey
import java.io.File
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

        log.info("Fetching properties and creating configuration for ${spec.entity}")

       val path = if (File(spec.tokenPath).exists()) {
            File(spec.tokenPath)
        } else {
            File(System.getProperty("user.home")).resolve(spec.tokenPath)
        }

        val token = path.readText(Charsets.UTF_8)

        val response = Unirest.get(spec.vaultUrl)
            .header("X-Vault-Token", token)
            .asJson()

        val secret = Gson().fromJson(response.body.toString(), VaultSecret::class.java)

        if (secret?.data == null) {
            if (!secret.errors.isNullOrEmpty()) {
                throw Error(secret.errors.joinToString(limit = 3))
            }
            throw IllegalArgumentException("Could not find secret for ${spec.entity}.")
        }

        val secretData = secret.data.data
        
        val signingKeyRef = DirectKeyRef(
            getKey(secretData, "public_signing_key", spec.entity).toJavaPublicKey(),
            getKey(secretData, "private_signing_key", spec.entity).toJavaPrivateKey()
        )
        
        val encryptionKeyRef = DirectKeyRef(
            getKey(secretData, "public_encryption_key", spec.entity).toJavaPublicKey(),
            getKey(secretData, "private_encryption_key", spec.entity).toJavaPrivateKey()
            
        )
        
        return DirectKeyEntity(
            mapOf(
                KeyType.SIGNING to signingKeyRef,
                KeyType.ENCRYPTION to encryptionKeyRef
            ),
            getKey(secretData, "private_signing_key", spec.entity).toJavaPrivateKey()) // Use signing key for all signing processes
    }

    private fun getKey(secretData: Map<String, Any>, keyName: String, originator: String): String {
        if (!secretData.containsKey(keyName)) {
            throw IllegalArgumentException("Missing $keyName key for $originator.")
        }

        return secretData[keyName].toString()
    }
}
