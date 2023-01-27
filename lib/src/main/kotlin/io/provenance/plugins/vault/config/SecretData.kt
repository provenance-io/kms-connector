package io.provenance.plugins.vault.config

data class SecretData(
    val data: Map<String, Any>,
    val metadata: Map<String, Any>
)
