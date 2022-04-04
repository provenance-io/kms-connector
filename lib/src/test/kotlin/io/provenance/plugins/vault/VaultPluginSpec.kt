package io.provenance.plugins.vault

import com.google.gson.Gson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.provenance.core.KeyType
import io.provenance.core.OriginatorManager
import java.util.UUID
import kong.unirest.GetRequest
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class VaultPluginSpec : WordSpec() {
    init {
        "plugin" should {
            "support valid config types" {
                VaultPlugin().supports(VaultSpec(UUID.randomUUID(), "", "")) shouldBe true
            }
            "not support other config types" {
                VaultPlugin().supports("") shouldBe false
            }
            "fetch secret" {
                val mnemonic = "Longing Rusted Seventeen Daybreak Furnace Nine Benign Homecoming One Freight Car"
                setup(
                    mapOf(
                        "private_mnemonic" to mnemonic,
                        "private_encryption_key" to "",
                        "public_encryption_key" to "",
                        "private_authorization_key" to "",
                        "public_authorization_key" to "",
                        "private_signature_key" to "",
                        "public_signature_key" to "",
                    )
                )

                val spec = VaultSpec(UUID.randomUUID(), "", "")
                val manager = OriginatorManager()
                manager.register(VaultPlugin())
                val originator = manager.get(spec.originatorUuid, spec)

                originator.keys[KeyType.MNEMONIC] as String shouldBe mnemonic
            }
            "throw if key is not present" {
                shouldThrow<IllegalArgumentException> {
                    setup()

                    val spec = VaultSpec(UUID.randomUUID(), "", "")
                    val manager = OriginatorManager()
                    manager.register(VaultPlugin())
                    manager.get(spec.originatorUuid, spec)
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