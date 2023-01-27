package io.provenance.plugins.fortanix

import com.fortanix.sdkms.v1.ApiClient
import com.fortanix.sdkms.v1.api.AuthenticationApi
import com.fortanix.sdkms.v1.api.SignAndVerifyApi
import com.fortanix.sdkms.v1.auth.ApiKeyAuth
import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity
import io.provenance.entity.fortanix.FortanixKeyEntity
import io.provenance.plugins.vault.VaultSpec
import io.provenance.scope.encryption.ecies.ECUtils
import io.provenance.scope.encryption.model.SmartKeyRef

class FortanixPlugin: Plugin {
    override fun supports(pluginSpec: Any): Boolean =
        when (pluginSpec) {
            is FortanixPluginSpec -> true
            else -> false
        }

    override fun fetch(pluginSpec: Any): KeyEntity {
        val spec = pluginSpec as FortanixPluginSpec
        
        val smartKeyClient = ApiClient().apply {
            setBasicAuthString(spec.apiKey)
            com.fortanix.sdkms.v1.Configuration.setDefaultApiClient(this)

            val authResponse = AuthenticationApi(this).authorize()
            val auth = getAuthentication("bearerToken") as ApiKeyAuth
            auth.apiKey = authResponse.accessToken
            auth.apiKeyPrefix = "Bearer"
        }
        
        val signAndVerifyApi = SignAndVerifyApi(smartKeyClient)
        val smartKeyRef = SmartKeyRef(
            ECUtils.convertBytesToPublicKey(spec.publicKey.toByteArray()),
            spec.uuid,
            signAndVerifyApi
        )
        
        return FortanixKeyEntity(smartKeyRef)
    }
}