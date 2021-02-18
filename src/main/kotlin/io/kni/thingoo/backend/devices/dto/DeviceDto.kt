package io.kni.thingoo.backend.devices.dto

import io.kni.thingoo.backend.entities.dto.EntityDto

data class DeviceDto(
    var id: Int,
    var deviceID: String,
    var macAddress: String,
    var displayName: String?,
    var entities: List<EntityDto>
)
