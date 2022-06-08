package io.provenance.plugins.vault

data class VaultSpec(
    val originator: String,
    val vaultUrl: String,
    val vaultToken: String,
)
