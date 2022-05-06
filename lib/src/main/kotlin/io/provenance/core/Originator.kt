package io.provenance.core

import io.provenance.scope.encryption.util.toJavaPublicKey

data class Originator(
    val keys: Map<KeyType, Any>,
) {
    fun signingPublicKey() = keys[KeyType.SIGNING_PUBLIC_KEY].toString().toJavaPublicKey()
    fun signingPrivateKey() = keys[KeyType.SIGNING_PRIVATE_KEY].toString().toJavaPublicKey()
    fun encryptionPublicKey() = keys[KeyType.ENCRYPTION_PUBLIC_KEY].toString().toJavaPublicKey()
    fun encryptionPrivateKey() = keys[KeyType.ENCRYPTION_PRIVATE_KEY].toString().toJavaPublicKey()
    fun authorizationPrivateKey() = keys[KeyType.AUTH_PRIVATE_KEY].toString().toJavaPublicKey()
    fun authorizationPublicKey() = keys[KeyType.AUTH_PUBLIC_KEY].toString().toJavaPublicKey()
}
