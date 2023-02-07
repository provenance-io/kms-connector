package io.provenance.entity.keystone

import io.provenance.entity.KeyEntity
import io.provenance.entity.KeyType
import io.provenance.scope.encryption.model.ApiKeyRef

class KeystoneKeyEntity(keyRefs: Map<KeyType, ApiKeyRef>) : KeyEntity(keyRefs) 