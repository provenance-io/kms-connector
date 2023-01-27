package io.provenance.entity.keystone

import io.provenance.entity.KeyEntity
import io.provenance.hdwallet.signer.BCECSigner
import io.provenance.scope.encryption.model.KeyRef
import java.security.PublicKey

class KeystoneKeyEntity(keyRef: KeyRef) : KeyEntity(keyRef) {
    override fun sign(data: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}