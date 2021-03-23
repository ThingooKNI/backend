package io.kni.thingoo.backend.entities.dto

import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType
import io.kni.thingoo.backend.icons.MaterialIcon

data class EntityDto(
    val id: Int,
    val key: String,
    val displayName: String?,
    val type: EntityType,
    val unitType: UnitType,
    val unitDisplayName: String,
    val icon: MaterialIcon?
)
