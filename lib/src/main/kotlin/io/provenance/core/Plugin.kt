package io.provenance.core

import io.provenance.entity.KeyEntity

interface Plugin <T: PluginConfig> {
    fun fetch(entity: String, config: T): KeyEntity
    fun supports(config: PluginConfig): Boolean
}