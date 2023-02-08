package io.provenance.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.provenance.scope.encryption.domain.inputstream.DIMEInputStream.Companion.configureProvenance

private val objectMapper by lazy {
    ObjectMapper()
        .configureProvenance()
}

fun Any.toPrettyJson(): String = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
