package io.provenance.core

import io.provenance.entity.KeyEntity

class KeyEntityManager {
    private val entities: MutableMap<String, KeyEntity> = mutableMapOf()
    private val plugins: MutableSet<Plugin<*>> = mutableSetOf()

    fun <T : PluginConfig> register(plugin: Plugin<T>) {
        plugins.add(plugin)
    }

    fun <T : PluginConfig> get(entity: String, config: T): KeyEntity {
        if (entity !in entities) {
            entities[entity] = plugins.filterIsInstance<Plugin<T>>().firstOrNull { it.supports(config) }?.fetch(entity, config)
                ?: throw IllegalStateException("$entity did not have exactly one supported plugin.")
        } 

        return entities[entity]
            ?: throw IllegalArgumentException("$entity is not a supported originator.")
    }
}