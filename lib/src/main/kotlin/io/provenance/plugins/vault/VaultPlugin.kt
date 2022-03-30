package io.provenance.plugins.vault

import com.google.gson.Gson
import io.provenance.core.KeyType
import io.provenance.core.Originator
import io.provenance.core.Plugin
import kong.unirest.Unirest
import mu.KotlinLogging

class VaultPlugin : Plugin {
    private val log = KotlinLogging.logger { }

    override fun supports(pluginSpec: Any): Boolean {
        return when (pluginSpec) {
            is VaultSpec -> true
            else -> false
        }
    }

    override fun fetch(pluginSpec: Any): Originator {
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
        if (!secretData.containsKey("private_mnemonic")) {
            throw IllegalArgumentException("Missing key mnemonic for ${spec.originatorUuid}.")
        }

        if (!secretData.containsKey("private_encryption_key")) {
            throw IllegalArgumentException("Missing private encryption key for ${spec.originatorUuid}.")
        }

        val keys = mapOf(
            KeyType.MNEMONIC to secretData["private_mnemonic"].toString(),
            KeyType.ENCRYPTION to secretData["private_encryption_key"].toString(),
        )

        return Originator(keys)
    }
}