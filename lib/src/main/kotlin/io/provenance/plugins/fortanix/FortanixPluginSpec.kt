package io.provenance.plugins.fortanix

import java.util.UUID

data class FortanixPluginSpec(
    val apiKey: String,
    val encryptionPublicKey: String,
    val signingPublicKey: String,
    val uuid: UUID,
)
