package io.provenance.plugins.keystone.model

data class SignatureRequest(
    val data: ByteArray,
    val signingType: SigningType,
)

enum class SigningType {
    P8E,
    PB
}

