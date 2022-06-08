package io.provenance.plugins.vault

data class SecretData(
    val data: Map<String, Any>,
    val metadata: Map<String, Any>
)
