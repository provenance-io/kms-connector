package io.provenance.plugins.vault

import com.google.gson.Gson
import io.provenance.core.KeyType
import io.provenance.core.Originator
import io.provenance.core.Plugin
import io.provenance.scope.encryption.util.toJavaPublicKey
import kong.unirest.Unirest
import mu.KotlinLogging

class VaultPlugin : Plugin {
    private val log = KotlinLogging.logger { }

    override fun supports(typeConfig: Any): Boolean {
        return when (typeConfig) {
            is VaultPluginConfig -> true
            else -> false
        }
    }

    override fun fetch(typeConfig: Any): Originator {
        val config = typeConfig as VaultPluginConfig

        log.info("Fetching properties and creating configuration for ${config.originatorUuid}")

        val response = Unirest.get(config.vaultUrl)
            .header("X-Vault-Token", config.vaultToken)
            .asJson()

        val secret = Gson().fromJson(response.body.toString(), VaultSecret::class.java)

        if (secret?.data == null) {
            if (!secret.errors.isNullOrEmpty()) {
                throw Error(secret.errors.joinToString(limit = 3))
            }
            throw IllegalArgumentException("Could not find secret for ${config.originatorUuid}.")
        }

        val secretData = secret.data.data
        if (!secretData.containsKey("private_mnemonic")) {
            throw IllegalArgumentException("Missing key mnemonic for ${config.originatorUuid}.")
        }

        if (!secretData.containsKey("private_encryption_key")) {
            throw IllegalArgumentException("Missing private encryption key for ${config.originatorUuid}.")
        }

        val keys = mapOf(
            KeyType.MNEMONIC to secretData["private_mnemonic"].toString(),
            KeyType.ENCRYPTION to secretData["private_encryption_key"].toString().toJavaPublicKey(),
        )

        return Originator(config.originatorUuid, keys)
    }
}