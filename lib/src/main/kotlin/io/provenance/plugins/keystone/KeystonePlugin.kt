package io.provenance.plugins.keystone

import io.provenance.core.Plugin
import io.provenance.entity.KeyEntity
import io.provenance.plugins.fortanix.FortanixPluginSpec

class KeystonePlugin: Plugin {
    override fun supports(pluginSpec: Any): Boolean =
        when (pluginSpec) {
            is KeystonePluginSpec -> true
            else -> false
        }
    
    override fun fetch(pluginSpec: Any): KeyEntity {
        TODO("Not yet implemented")
    }
}