package io.provenance.core

import io.provenance.entity.KeyEntity
import java.util.UUID

class EntityManager {
    private val entities: MutableMap<UUID, KeyEntity> = mutableMapOf()
    private val plugins: MutableSet<Plugin> = mutableSetOf()

    fun register(plugin: Plugin) =
        plugins.add(plugin)

    fun get(entityUuid: UUID, pluginSpec: Any): KeyEntity {
        if (entityUuid !in entities) {
            entities[entityUuid] = plugins.firstOrNull { it.supports(pluginSpec) }?.fetch(pluginSpec)
                ?: throw IllegalStateException("$entityUuid has no supported plugins.")
        }

        return entities[entityUuid]
            ?: throw IllegalArgumentException("$entityUuid is not a supported originator.")
    }
}