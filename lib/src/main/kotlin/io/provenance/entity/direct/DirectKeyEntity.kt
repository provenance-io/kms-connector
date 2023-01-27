package io.provenance.entity.direct

import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.hdwallet.common.hashing.sha256
import io.provenance.hdwallet.ec.extensions.toECPrivateKey
import io.provenance.hdwallet.signer.BCECSigner
import io.provenance.scope.encryption.model.DirectKeyRef
import java.security.PrivateKey

class DirectKeyEntity(
    keyRefs: Map<KeyType, DirectKeyRef>,
    private val privateKey: PrivateKey,
) : KeyEntity(keyRefs) {
    
    override fun sign(keyType: KeyType, data: ByteArray): ByteArray =
        BCECSigner().sign(privateKey.toECPrivateKey(), data.sha256())
            .encodeAsBTC()
            .toByteArray()
}