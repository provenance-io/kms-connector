package io.provenance.plugins.vault

import com.google.gson.Gson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.provenance.core.KeyEntityManager
import io.provenance.core.Plugin
import io.provenance.core.PluginConfig
import io.provenance.entity.KeyType
import io.provenance.plugins.vault.config.SecretData
import io.provenance.plugins.vault.config.VaultSecret
import io.provenance.scope.encryption.util.toJavaPublicKey
import java.io.File
import kong.unirest.GetRequest
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VaultPluginSpec : WordSpec() {
    init {
        "plugin" should {
            val token = withContext(Dispatchers.IO) {
                File.createTempFile("temp", null)
            }

            "fetch secret" {
                val publicKeyHex = "0A4104D7820B3244C3F72A1D2631E089E6C40D7D8C88221E771ED631402AC025E59D9CFF82078F4492E231691A6C4D1D36F085CD7B3ED699C35C685E462E4106C13A1C"
                val privateKeyHex = "0A207EA5368D527F633A76EA43EC6103574C72BB9175A6C8D381D8403CAD70A928B6"

                setup(
                    mapOf(
                        "private_encryption_key" to privateKeyHex,
                        "public_encryption_key" to publicKeyHex,
                        "private_signing_key" to privateKeyHex,
                        "public_signing_key" to publicKeyHex,
                    )
                )

                val spec = VaultConfig("", token.absolutePath)
                val manager = KeyEntityManager()
                manager.register(VaultPlugin())
                val originator = manager.get("test", spec)

                originator.getKeyRef(KeyType.SIGNING).publicKey shouldBe publicKeyHex.toJavaPublicKey()
            }
            "throw if key is not present" {
                shouldThrow<IllegalArgumentException> {
                    setup()

                    val spec = VaultConfig("", token.absolutePath)
                    val manager = KeyEntityManager()
                    
                    manager.register(VaultPlugin())
                    manager.get("", spec)
                }
            }
        }
    }

    private fun setup(keyMap: Map<String, Any> = emptyMap()) {
        val secret = VaultSecret(
            null, null, null,
            data = SecretData(
                keyMap,
                metadata = emptyMap()
            ),
            null, null
        )
        val mockGetRequest = mockk<GetRequest>()
        val mockResponse = mockk<HttpResponse<JsonNode>>()

        mockkStatic(Unirest::get)
        every { Unirest.get("") } returns mockGetRequest
        every { mockGetRequest.header(any(), any()) } returns mockGetRequest
        every { mockGetRequest.asJson() } returns mockResponse
        every { mockResponse.body } returns JsonNode(Gson().toJson(secret))
    }
}