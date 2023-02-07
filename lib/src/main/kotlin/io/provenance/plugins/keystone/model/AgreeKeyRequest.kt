package io.provenance.plugins.keystone.model

data class AgreeKeyRequest(
    val id: String,
    val publicKey: ByteArray,
    val addressIndex: Int = 0,
) 