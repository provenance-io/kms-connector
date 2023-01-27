package io.provenance.plugins.fortanix

import java.util.UUID

data class FortanixPluginSpec(
    val apiKey: String,
    val publicKey: String,
    val uuid: UUID,
)
