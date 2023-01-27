package io.provenance.entity

import io.provenance.scope.encryption.model.KeyRef
import io.provenance.scope.encryption.util.getAddress
import java.security.PublicKey

abstract class KeyEntity(
    protected val keyRefs: Map<KeyType, KeyRef>
) {
    open fun getKeyRef(keyType: KeyType) = keyRefs[keyType] 
        ?: throw IllegalArgumentException("No such key type present")
    
    open fun sign(keyType: KeyType, data: ByteArray): ByteArray =
        keyRefs[keyType]?.signer()?.sign(data)?.signature?.toByteArray() 
            ?: throw IllegalArgumentException("No such key type present")
    
    open fun publicKey(keyType: KeyType): PublicKey =
        keyRefs[keyType]?.signer()?.getPublicKey()
            ?: throw IllegalArgumentException("No such key type present")
    
    open fun address(keyType: KeyType, isMainNet: Boolean): String =
        keyRefs[keyType]?.publicKey?.getAddress(isMainNet)
            ?: throw IllegalArgumentException("No such key type present")
    
}  