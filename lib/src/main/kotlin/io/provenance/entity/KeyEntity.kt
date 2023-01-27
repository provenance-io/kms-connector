package io.provenance.entity

import io.provenance.scope.encryption.model.KeyRef
import io.provenance.scope.encryption.util.getAddress
import java.security.PublicKey

abstract class KeyEntity(
    protected val keyRef: KeyRef
) {
    open fun sign(data: ByteArray): ByteArray =
        keyRef.signer().sign(data).signature.toByteArray()
    
    open fun publicKey(): PublicKey = 
        keyRef.signer().getPublicKey()
    
    open fun address(isMainNet: Boolean): String =
        keyRef.publicKey.getAddress(isMainNet)
} 