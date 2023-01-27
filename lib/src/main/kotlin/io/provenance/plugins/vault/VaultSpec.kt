package io.provenance.plugins.vault

data class VaultSpec(
    val entity: String,
    val vaultUrl: String,
    val tokenPath: String,
)
