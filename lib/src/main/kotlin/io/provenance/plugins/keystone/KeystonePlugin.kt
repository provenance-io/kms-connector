package io.provenance.plugins.keystone

import com.google.common.io.BaseEncoding
import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.entity.keystone.KeystoneKeyEntity
import io.provenance.scope.encryption.ecies.ECUtils
import io.provenance.scope.encryption.model.ApiKeyRef

class KeystonePlugin : Plugin<KeystoneConfig> {
    override fun fetch(entity: String, config: KeystoneConfig): KeyEntity {
        val client = KeystoneClient(entity, config.apiKey, config.url)

        val encryptionKeyRef = ApiKeyRef(ECUtils.convertBytesToPublicKey(BaseEncoding.base64().decode(config.signingPublicKey)), client)
        val signingKeyRef = ApiKeyRef(ECUtils.convertBytesToPublicKey(BaseEncoding.base64().decode(config.encryptionPublicKey)), client)

        return KeystoneKeyEntity(
            mapOf(
                KeyType.SIGNING to signingKeyRef,
                KeyType.ENCRYPTION to encryptionKeyRef
            )
        )
    }
}