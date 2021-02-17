package io.kni.thingoo.backend.entities

data class RegisterEntityDto(
    var key: String,
    var displayName: String?,
    var type: EntityType,
    var unitType: UnitType,
    var unitDisplayName: String
) {
    fun toEntity(): Entity {
        return Entity(
            id = 0,
            key,
            displayName,
            type,
            unitType,
            unitDisplayName,
        )
    }
}
