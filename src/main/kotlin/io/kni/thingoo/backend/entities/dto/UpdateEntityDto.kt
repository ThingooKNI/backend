package io.kni.thingoo.backend.entities.dto

import io.kni.thingoo.backend.icons.MaterialIcon

data class UpdateEntityDto(
    var displayName: String?,
    var icon: MaterialIcon?
)
