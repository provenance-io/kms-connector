package io.provenance.plugins.keystone

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.provenance.plugins.keystone.model.AgreeKeyRequest
import io.provenance.plugins.keystone.model.AgreeKeyResponse
import io.provenance.plugins.keystone.model.SignatureRequest
import io.provenance.plugins.keystone.model.SignatureResponse
import io.provenance.plugins.keystone.model.SigningType
import io.provenance.scope.encryption.crypto.ApiSignerClient
import io.provenance.scope.encryption.domain.inputstream.DIMEInputStream.Companion.configureProvenance
import io.provenance.scope.encryption.ecies.ECUtils
import java.security.PublicKey
import java.util.UUID
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

class KeystoneClient(
    private val entity: String,
    private val apikey: String,
    url: String,
) : ApiSignerClient {
    private val client: KeystoneApi = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(
            JacksonConverterFactory.create(
                ObjectMapper()
                    .configureProvenance()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false),
            )
        )
        .build()
        .create(KeystoneApi::class.java)

    override fun sign(data: ByteArray): ByteArray {
        val request = SignatureRequest(data, SigningType.PB)

        return runBlocking {

            val response = client.sign(apikey, entity, 0, request)

            if (!response.isSuccessful) {
                throw IllegalStateException("Failed to sign with error ${response.errorBody()}")
            }

            response.body()?.signatureBytes!!
        }
    }

    override fun secretKey(ephemeralPublicKey: PublicKey): ByteArray {
        val request = AgreeKeyRequest(entity, ECUtils.convertPublicKeyToBytes(ephemeralPublicKey), 0)

        return runBlocking {
            val response = client.secretKey(apikey, request)

            if (!response.isSuccessful) {
                throw IllegalStateException("Failed to retrieve secret key with error ${response.errorBody()}")
            }

            response.body()?.agreeKey!!
        }
    }
}

interface KeystoneApi {
    @POST("sign/member/{memberUuid}/address/{addressIndex}")
    suspend fun sign(
        @Header("apikey") apikey: String,
        @Path("memberUuid") member: String,
        @Path("addressIndex") index: Int,
        @Body signatureRequest: SignatureRequest,
    ): Response<SignatureResponse>

    @POST("agree/key")
    suspend fun secretKey(
        @Header("apikey") apikey: String,
        @Body agreeKeyRequest: AgreeKeyRequest,
    ): Response<AgreeKeyResponse>
}