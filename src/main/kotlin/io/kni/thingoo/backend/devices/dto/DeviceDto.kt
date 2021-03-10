package io.kni.thingoo.backend.devices.dto

import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.icons.MaterialIcon

data class DeviceDto(
    var id: Int,
    var key: String,
    var macAddress: String,
    var displayName: String?,
    var icon: MaterialIcon?,
    var entities: List<EntityDto>,
)
