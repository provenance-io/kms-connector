package io.provenance.core

interface Plugin {
    fun supports(pluginSpec: Any): Boolean
    fun fetch(pluginSpec: Any): Originator
}