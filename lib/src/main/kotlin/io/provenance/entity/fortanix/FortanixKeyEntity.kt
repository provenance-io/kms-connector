package io.provenance.entity.fortanix

import com.fortanix.sdkms.v1.ApiClient
import com.fortanix.sdkms.v1.api.AuthenticationApi
import com.fortanix.sdkms.v1.auth.ApiKeyAuth
import io.provenance.entity.KeyEntity
import io.provenance.scope.encryption.model.SmartKeyRef
import java.security.PublicKey

class FortanixKeyEntity(smartKeyRef: SmartKeyRef) : KeyEntity(smartKeyRef) {
    override fun sign(data: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}