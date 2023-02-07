package io.provenance.plugins.keystone

import io.provenance.core.PluginConfig

data class KeyStoneConfig(
    val encryptionPublicKey: String,
    val signingPublicKey: String,
    val apiKey: String
) : PluginConfig()