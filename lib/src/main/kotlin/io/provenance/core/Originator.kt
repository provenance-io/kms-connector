package io.provenance.core

import java.util.UUID

data class Originator(
    val uuid: UUID,
    val keys: Map<KeyType, Any>,
)