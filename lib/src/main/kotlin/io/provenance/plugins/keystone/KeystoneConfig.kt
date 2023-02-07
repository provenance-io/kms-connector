package io.provenance.plugins.keystone

import io.provenance.core.PluginConfig

data class KeystoneConfig(
    val encryptionPublicKey: String,
    val signingPublicKey: String,
    val apiKey: String,
    val url: String
) : PluginConfig()