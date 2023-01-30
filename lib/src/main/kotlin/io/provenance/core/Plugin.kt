package io.provenance.core

import io.provenance.entity.KeyEntity

interface Plugin {
    fun supports(pluginSpec: Any): Boolean
    fun fetch(pluginSpec: Any): KeyEntity
}