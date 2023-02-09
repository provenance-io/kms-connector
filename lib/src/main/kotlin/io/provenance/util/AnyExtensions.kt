package io.provenance.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.provenance.scope.encryption.domain.inputstream.DIMEInputStream.Companion.configureProvenance
import retrofit2.Response

private val objectMapper by lazy {
    ObjectMapper()
        .configureProvenance()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
}

fun Any.toPrettyJson(): String = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)

fun <T> Response<T>.errorBodyOrMessage() = this.errorBody()?.toString() ?: this.message()
