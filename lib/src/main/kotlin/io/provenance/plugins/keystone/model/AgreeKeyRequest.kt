package io.provenance.plugins.keystone.model

import java.util.UUID

data class AgreeKeyRequest(
    val id: String,
    val publicKey: ByteArray,
    val addressIndex: Int = 0,
) 