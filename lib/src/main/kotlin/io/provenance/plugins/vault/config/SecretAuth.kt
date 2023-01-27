package io.provenance.plugins.vault.config

data class SecretAuth(
    val clientToken: String,
    val accessor: String,
    val policies: List<String>,
    val metadata: Map<String, String>,
    val leaseDuration: Int,
    val renewable: Boolean
)