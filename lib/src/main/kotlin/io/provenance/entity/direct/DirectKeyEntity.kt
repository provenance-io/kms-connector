package io.provenance.entity.direct

import io.provenance.entity.KeyEntity
import io.provenance.hdwallet.common.hashing.sha256
import io.provenance.hdwallet.ec.extensions.toECPrivateKey
import io.provenance.hdwallet.signer.BCECSigner
import io.provenance.scope.encryption.model.DirectKeyRef
import java.security.PrivateKey

class DirectKeyEntity(
    directKeyRef: DirectKeyRef,
    private val privateKey: PrivateKey,
) : KeyEntity(directKeyRef) {
    
    override fun sign(data: ByteArray): ByteArray =
        BCECSigner().sign(privateKey.toECPrivateKey(), data.sha256())
            .encodeAsBTC()
            .toByteArray()
}