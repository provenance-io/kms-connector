package io.provenance.plugins.keystone.model

data class SignatureResponse(
    val publicKey: Key,
    val signatureBytes: ByteArray,
)

data class Key(
    val address: Address,
    val encodedKey: ByteArray,
    val curve: String,
    val encoding: String,
)