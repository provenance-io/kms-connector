package io.provenance.plugins.fortanix

import com.fortanix.sdkms.v1.ApiClient
import com.fortanix.sdkms.v1.api.AuthenticationApi
import com.fortanix.sdkms.v1.api.SignAndVerifyApi
import com.fortanix.sdkms.v1.auth.ApiKeyAuth
import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.entity.fortanix.FortanixKeyEntity
import io.provenance.scope.encryption.ecies.ECUtils
import io.provenance.scope.encryption.model.SmartKeyRef
import io.provenance.scope.util.toUuid

class FortanixPlugin: Plugin<FortanixConfig> {
    override fun fetch(entity: String, config: FortanixConfig): KeyEntity {
        
        val smartKeyClient = ApiClient().apply {
            setBasicAuthString(config.apiKey)
            com.fortanix.sdkms.v1.Configuration.setDefaultApiClient(this)

            val authResponse = AuthenticationApi(this).authorize()
            val auth = getAuthentication("bearerToken") as ApiKeyAuth
            auth.apiKey = authResponse.accessToken
            auth.apiKeyPrefix = "Bearer"
        }
        
        val signAndVerifyApi = SignAndVerifyApi(smartKeyClient)
        val encryptionKeyRef = SmartKeyRef(
            ECUtils.convertBytesToPublicKey(config.encryptionPublicKey.toByteArray()),
            entity.toUuid(),
            signAndVerifyApi
        )

        val signingKeyRef = SmartKeyRef(
            ECUtils.convertBytesToPublicKey(config.signingPublicKey.toByteArray()),
            entity.toUuid(),
            signAndVerifyApi
        )
        
        return FortanixKeyEntity(
            mapOf(
                KeyType.SIGNING to signingKeyRef,
                KeyType.ENCRYPTION to encryptionKeyRef
            )
        )
    }
}