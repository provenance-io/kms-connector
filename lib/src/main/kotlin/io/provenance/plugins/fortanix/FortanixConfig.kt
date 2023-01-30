package io.provenance.plugins.fortanix

import io.provenance.core.PluginConfig
import java.util.UUID

data class FortanixConfig(
    val apiKey: String,
    val encryptionPublicKey: String,
    val signingPublicKey: String,
) : PluginConfig()
