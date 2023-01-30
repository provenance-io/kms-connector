package io.provenance.plugins.keystone

import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity

class KeystonePlugin: Plugin<KeyStoneConfig> {
    override fun fetch(entity: String, config: KeyStoneConfig): KeyEntity {
        TODO("Not yet implemented")
    }
}