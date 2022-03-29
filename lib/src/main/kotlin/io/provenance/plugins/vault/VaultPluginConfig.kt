package io.provenance.plugins.vault

import java.util.UUID

data class VaultPluginConfig(
    val originatorUuid: UUID,
    val vaultUrl: String,
    val vaultToken: String,
)