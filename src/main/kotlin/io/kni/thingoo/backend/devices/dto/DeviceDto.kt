package io.kni.thingoo.backend.devices.dto

import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.icons.MaterialIcon

data class DeviceDto(
    val id: Int,
    val key: String,
    val macAddress: String,
    val displayName: String?,
    val icon: MaterialIcon?,
    val entities: List<EntityDto>,
)
