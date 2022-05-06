package io.provenance.core

import io.provenance.scope.encryption.util.toJavaPrivateKey
import io.provenance.scope.encryption.util.toJavaPublicKey
import java.security.Key

class Originator(
    val keys: Map<KeyType, Any>,
) {
    private val keyMap = mutableMapOf<KeyType, Key>()

    fun signingPublicKey() = getKey(KeyType.SIGNING_PUBLIC_KEY) { it.toJavaPublicKey() }
    fun signingPrivateKey() = getKey(KeyType.SIGNING_PRIVATE_KEY) { it.toJavaPrivateKey() }
    fun encryptionPublicKey() = getKey(KeyType.ENCRYPTION_PUBLIC_KEY) { it.toJavaPublicKey() }
    fun encryptionPrivateKey() = getKey(KeyType.ENCRYPTION_PRIVATE_KEY) { it.toJavaPrivateKey() }
    fun authorizationPrivateKey() = getKey(KeyType.AUTH_PRIVATE_KEY) { it.toJavaPrivateKey() }
    fun authorizationPublicKey() = getKey(KeyType.AUTH_PUBLIC_KEY) { it.toJavaPublicKey() }

    private fun getKey(keyType: KeyType, transform: (String) -> Key) = keyMap.getOrPut(keyType) {
        keys[keyType].toString().let(transform)
    }
}
