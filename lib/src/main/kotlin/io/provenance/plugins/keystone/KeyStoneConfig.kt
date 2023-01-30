package io.provenance.plugins.keystone

import io.provenance.core.PluginConfig

data class KeyStoneConfig(
    val url: String
) : PluginConfig()