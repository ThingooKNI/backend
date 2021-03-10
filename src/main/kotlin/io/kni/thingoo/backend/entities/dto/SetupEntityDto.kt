package io.kni.thingoo.backend.entities.dto

import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType

data class SetupEntityDto(
    var key: String,
    var type: EntityType,
    var unitType: UnitType,
    var unitDisplayName: String
) {
    fun toEntity(): Entity {
        return Entity(
            id = 0,
            key,
            null,
            type,
            unitType,
            unitDisplayName,
        )
    }
}
