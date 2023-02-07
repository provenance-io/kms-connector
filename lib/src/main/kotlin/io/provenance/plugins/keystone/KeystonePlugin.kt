package io.provenance.plugins.keystone

import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.entity.keystone.KeystoneKeyEntity
import io.provenance.scope.encryption.ecies.ECUtils
import io.provenance.scope.encryption.model.ApiKeyRef

class KeystonePlugin: Plugin<KeyStoneConfig> {
    override fun fetch(entity: String, config: KeyStoneConfig): KeyEntity {
        val client = KeystoneClient(entity, config.apiKey)
        
        val encryptionKeyRef = ApiKeyRef(ECUtils.convertBytesToPublicKey(config.signingPublicKey.toByteArray()), client)
        val signingKeyRef = ApiKeyRef(ECUtils.convertBytesToPublicKey(config.encryptionPublicKey.toByteArray()), client)
        
        return KeystoneKeyEntity(
            mapOf(
                KeyType.SIGNING to signingKeyRef,
                KeyType.ENCRYPTION to encryptionKeyRef
            )
        )
    }
}