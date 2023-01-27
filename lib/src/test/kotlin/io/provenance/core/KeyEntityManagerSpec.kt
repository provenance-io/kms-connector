package io.provenance.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.provenance.entity.KeyType
import io.provenance.entity.direct.DirectKeyEntity
import io.provenance.plugins.vault.VaultSpec
import io.provenance.scope.encryption.model.DirectKeyRef
import io.provenance.scope.encryption.util.toJavaPrivateKey
import io.provenance.scope.encryption.util.toJavaPublicKey
import java.security.KeyPair

class KeyEntityManagerSpec : WordSpec() {
    init {
        "manager" should {
            "throw if plugin is not registered" {
                shouldThrow<IllegalStateException> {
                    KeyEntityManager().get("test", "")
                }
            }
            "fetch secret from plugin only once" {
                val mockPlugin = mockk<Plugin>()
                val kp = KeyPair(
                    "0A4104D7820B3244C3F72A1D2631E089E6C40D7D8C88221E771ED631402AC025E59D9CFF82078F4492E231691A6C4D1D36F085CD7B3ED699C35C685E462E4106C13A1C".toJavaPublicKey(),
                    "0A207EA5368D527F633A76EA43EC6103574C72BB9175A6C8D381D8403CAD70A928B6".toJavaPrivateKey()
                )
                
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