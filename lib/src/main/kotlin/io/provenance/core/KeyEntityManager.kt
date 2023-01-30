package io.provenance.core

import io.provenance.entity.KeyEntity
import java.util.UUID

class KeyEntityManager {
    private val entities: MutableMap<String, KeyEntity> = mutableMapOf()
    private val plugins: MutableSet<Plugin> = mutableSetOf()

    fun register(plugin: Plugin) =
        plugins.add(plugin)

    fun get(entity: String, pluginSpec: Any): KeyEntity {
        if (entity !in entities) {
            entities[entity] = plugins.firstOrNull { it.supports(pluginSpec) }?.fetch(pluginSpec)
                ?: throw IllegalStateException("$entity has no supported plugins.")
        }

        return entities[entity]
            ?: throw IllegalArgumentException("$entity is not a supported originator.")
    }
}