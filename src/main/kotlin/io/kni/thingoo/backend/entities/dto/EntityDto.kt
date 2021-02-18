package io.kni.thingoo.backend.entities.dto

import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType

data class EntityDto(
    var id: Int,
    var key: String,
    var displayName: String?,
    var type: EntityType,
    var unitType: UnitType,
    var unitDisplayName: String
)
