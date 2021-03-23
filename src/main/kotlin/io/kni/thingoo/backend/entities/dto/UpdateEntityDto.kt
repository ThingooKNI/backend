package io.kni.thingoo.backend.entities.dto

import io.kni.thingoo.backend.icons.MaterialIcon

data class UpdateEntityDto(
    val displayName: String?,
    val icon: MaterialIcon?
)
