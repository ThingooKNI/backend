package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.entities.RegisterEntityDto

data class RegisterDeviceDto(
    var deviceID: String,
    var macAddress: String,
    var displayName: String,
    var entities: Set<RegisterEntityDto>
)
