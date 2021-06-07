package io.kni.thingoo.backend.entities.dto

import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.ValueType

data class SetupEntityDto(
    val key: String,
    val type: EntityType,
    val valueType: ValueType,
    val unitDisplayName: String
) {
    fun toEntity(): Entity {
        return Entity(
            id = 0,
            key,
            null,
            type,
            valueType,
            unitDisplayName,
            null
        )
    }
}
