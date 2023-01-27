package io.provenance.entity.fortanix

import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.scope.encryption.model.SmartKeyRef

class FortanixKeyEntity(keyRefs: Map<KeyType, SmartKeyRef>) : KeyEntity(keyRefs) 