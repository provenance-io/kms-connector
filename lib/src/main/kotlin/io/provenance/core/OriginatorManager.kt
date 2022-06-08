package io.provenance.core

class OriginatorManager {
    private val originators: MutableMap<String, Originator> = mutableMapOf()
    private val plugins: MutableSet<Plugin> = mutableSetOf()

    fun register(plugin: Plugin) =
        plugins.add(plugin)

    fun get(originator: String, pluginSpec: Any): Originator {
        if (originator !in originators) {
            originators[originator] = plugins.firstOrNull { it.supports(pluginSpec) }?.fetch(pluginSpec)
                ?: throw IllegalStateException("$originator has no supported plugins.")
        }

        return originators[originator]
            ?: throw IllegalArgumentException("$originator is not a supported originator.")
    }
}
