package io.provenance.core

interface Plugin {
    fun supports(typeConfig: Any): Boolean
    fun fetch(typeConfig: Any): Originator
}