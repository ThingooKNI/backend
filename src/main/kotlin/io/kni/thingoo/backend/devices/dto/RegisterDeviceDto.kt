package io.kni.thingoo.backend.devices.dto

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.entities.dto.RegisterEntityDto

data class RegisterDeviceDto(
    var key: String,
    var macAddress: String,
    var entities: List<RegisterEntityDto>
) {
    fun toDevice(): Device {
        return Device(
            id = 0,
            key = key,
            macAddress = macAddress,
            displayName = null,
            entities = entities.map { it.toEntity() }.toMutableList()
        )
    }
}
