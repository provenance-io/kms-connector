package io.provenance.core

import io.provenance.entity.KeyEntity

class KeyEntityManager {
    private val entities: MutableMap<String, KeyEntity> = mutableMapOf()
    private val plugins: MutableSet<Plugin<*>> = mutableSetOf()

    fun register(plugin: Plugin<*>) =
        plugins.add(plugin)

    fun <T: PluginConfig> get(entity: String, config: T): KeyEntity {
        if (entity !in entities) {
            entities[entity] = plugins.filterIsInstance<Plugin<T>>().singleOrNull()?.fetch(entity, config)
                ?: throw IllegalStateException("$entity has no supported plugins.")
        }

        return entities[entity]
            ?: throw IllegalArgumentException("$entity is not a supported originator.")
    }
}