package io.provenance.core

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.provenance.plugins.fortanix.FortanixConfig
import io.provenance.plugins.keystone.KeystoneConfig
import io.provenance.plugins.vault.VaultConfig

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(VaultConfig::class),
    JsonSubTypes.Type(FortanixConfig::class),
    JsonSubTypes.Type(KeystoneConfig::class),
)
abstract class PluginConfig

