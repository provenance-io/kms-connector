package io.provenance.core

import io.provenance.scope.encryption.util.toJavaPublicKey
import java.security.Key

class Originator(
    val keys: Map<KeyType, Any>,
) {
    private val keyMap = mutableMapOf<KeyType, Key>()

    fun signingPublicKey() = getKey(KeyType.SIGNING_PUBLIC_KEY)
    fun signingPrivateKey() = getKey(KeyType.SIGNING_PRIVATE_KEY)
    fun encryptionPublicKey() = getKey(KeyType.ENCRYPTION_PUBLIC_KEY)
    fun encryptionPrivateKey() = getKey(KeyType.ENCRYPTION_PRIVATE_KEY)
    fun authorizationPrivateKey() = getKey(KeyType.AUTH_PRIVATE_KEY)
    fun authorizationPublicKey() = getKey(KeyType.AUTH_PUBLIC_KEY)

    private fun getKey(keyType: KeyType) = keyMap.getOrDefault(keyType, null)?.let {
        keys[KeyType.SIGNING_PUBLIC_KEY].toString().toJavaPublicKey()
    }?.also {
        keyMap[keyType] = it
    }
}
