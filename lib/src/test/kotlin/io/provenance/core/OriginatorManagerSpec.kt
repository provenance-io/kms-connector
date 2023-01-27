package io.provenance.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.provenance.plugins.vault.VaultSpec
import java.util.UUID

class OriginatorManagerSpec : WordSpec() {
    init {
        "manager" should {
            "throw if plugin is not registered" {
                shouldThrow<IllegalStateException> {
                    OriginatorManager().get("test", "")
                }
            }
            "fetch secret from plugin only once" {
                val mockPlugin = mockk<Plugin>()

                every { mockPlugin.supports(any()) } returns true
                every { mockPlugin.fetch(any()) } returns Originator(emptyMap())

                val spec = VaultSpec("test", "", "")
                val manager = OriginatorManager()
                manager.register(mockPlugin)
                manager.get(spec.originator, spec)

                verify(exactly = 1) { mockPlugin.fetch(any()) }
                verify(exactly = 1) { mockPlugin.supports(any()) }

                manager.get(spec.originator, spec)

                verify(exactly = 1) { mockPlugin.fetch(any()) }
                verify(exactly = 1) { mockPlugin.supports(any()) }
            }
        }
    }
}