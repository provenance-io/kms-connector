package io.provenance.plugins.vault

import io.provenance.core.PluginConfig

data class VaultConfig(
    val vaultUrl: String,
    val tokenPath: String,
) : PluginConfig()
