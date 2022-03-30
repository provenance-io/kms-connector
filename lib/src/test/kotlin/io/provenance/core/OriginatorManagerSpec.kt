package io.provenance.core

import com.google.gson.Gson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.provenance.plugins.vault.SecretData
import io.provenance.plugins.vault.VaultPlugin
import io.provenance.plugins.vault.VaultSecret
import io.provenance.plugins.vault.VaultSpec
import java.security.PublicKey
import java.util.UUID
import kong.unirest.GetRequest
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class OriginatorManagerSpec : WordSpec() {
    init {
        "manager" should {
            "throw if plugin is not registered" {
                shouldThrow<IllegalStateException> {
                    OriginatorManager().get(UUID.randomUUID(), "")
                }
            }
            "fetch secret from plugin only once" {
                val mockPlugin = mockk<Plugin>()

                every { mockPlugin.supports(any()) } returns true
                every { mockPlugin.fetch(any()) } returns Originator(emptyMap())

                val spec = VaultSpec(UUID.randomUUID(), "", "")
                val manager = OriginatorManager()
                manager.register(mockPlugin)
                manager.get(spec.originatorUuid, spec)

                verify(exactly = 1) { mockPlugin.fetch(any()) }
                verify(exactly = 1) { mockPlugin.supports(any()) }

                manager.get(spec.originatorUuid, spec)

                verify(exactly = 1) { mockPlugin.fetch(any()) }
                verify(exactly = 1) { mockPlugin.supports(any()) }
            }
        }
    }
}