package io.provenance.core

import java.util.UUID
import mu.KotlinLogging

class OriginatorManager {
    private val log = KotlinLogging.logger { }
    private val originators: MutableMap<UUID, Originator> = mutableMapOf()
    private val plugins: MutableSet<Plugin> = mutableSetOf()

    fun register(plugin: Plugin) =
        plugins.add(plugin)

    fun get(originatorUuid: UUID, typeConfig: Any): Originator {
        if (originatorUuid !in originators) {
            plugins.firstOrNull { it.supports(typeConfig) }?.fetch(typeConfig)
                ?: throw IllegalStateException("$originatorUuid has no supported plugins.")
        }

        return originators[originatorUuid] ?: throw IllegalArgumentException("$originatorUuid is not a supported originator.")
    }
}