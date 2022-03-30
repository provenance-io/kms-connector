package io.provenance.plugins.vault

import java.util.UUID

data class VaultSpec(
    val originatorUuid: UUID,
    val vaultUrl: String,
    val vaultToken: String,
)