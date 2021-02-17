package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.entities.RegisterEntityDto

data class RegisterDeviceDto(
    var deviceID: String,
    var macAddress: String,
    var displayName: String?,
    var entities: List<RegisterEntityDto>
) {
    fun toDevice(): Device {
        return Device(
            id = 0,
            deviceID,
            macAddress,
            displayName,
            entities = entities.map { it.toEntity() }.toMutableList()
        )
    }
}
