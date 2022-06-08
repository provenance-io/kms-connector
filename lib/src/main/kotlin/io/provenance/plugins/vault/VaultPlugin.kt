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

        log.info("Fetching properties and creating configuration for ${spec.originator}")

        val response = Unirest.get(spec.vaultUrl)
            .header("X-Vault-Token", spec.vaultToken)
            .asJson()

        val secret = Gson().fromJson(response.body.toString(), VaultSecret::class.java)

        if (secret?.data == null) {
            if (!secret.errors.isNullOrEmpty()) {
                throw Error(secret.errors.joinToString(limit = 3))
            }
            throw IllegalArgumentException("Could not find secret for ${spec.originator}.")
        }

        val secretData = secret.data.data

        val keys = mapOf(
            KeyType.ENCRYPTION_PRIVATE_KEY to getKey(secretData, "private_encryption_key", spec.originator),
            KeyType.ENCRYPTION_PUBLIC_KEY to getKey(secretData, "public_encryption_key", spec.originator),
            KeyType.SIGNING_PRIVATE_KEY to getKey(secretData, "private_signing_key", spec.originator),
            KeyType.SIGNING_PUBLIC_KEY to getKey(secretData, "public_signing_key", spec.originator)
        )

        return Originator(keys)
    }

    private fun getKey(secretData: Map<String, Any>, keyName: String, originator: String): String {
        if (!secretData.containsKey(keyName)) {
            throw IllegalArgumentException("Missing $keyName key for $originator.")
        }

        return secretData[keyName].toString()
    }
}
