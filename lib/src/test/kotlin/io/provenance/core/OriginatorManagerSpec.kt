package io.provenance.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.entity.direct.DirectKeyEntity
import io.provenance.plugins.vault.VaultSpec
import io.provenance.scope.encryption.ecies.ECUtils
import io.provenance.scope.encryption.model.DirectKeyRef
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.swing.text.html.parser.Entity

class OriginatorManagerSpec : WordSpec() {
    init {
        "manager" should {
            "throw if plugin is not registered" {
                shouldThrow<IllegalStateException> {
                    KeyEntityManager().get("test", "")
                }
            }
            "fetch secret from plugin only once" {
                val mockPlugin = mockk<Plugin>()
                val kp = KeyPairGenerator.getInstance("2048").generateKeyPair()
                every { mockPlugin.supports(any()) } returns true
                every { mockPlugin.fetch(any()) } returns DirectKeyEntity(mapOf(KeyType.SIGNING to DirectKeyRef(kp)), kp.private)

                val spec = VaultSpec("test", "", "")
                val manager = KeyEntityManager()
                manager.register(mockPlugin)
                manager.get(spec.entity, spec)

                verify(exactly = 1) { mockPlugin.fetch(any()) }
                verify(exactly = 1) { mockPlugin.supports(any()) }

                manager.get(spec.entity, spec)

                verify(exactly = 1) { mockPlugin.fetch(any()) }
                verify(exactly = 1) { mockPlugin.supports(any()) }
            }
        }
    }
}