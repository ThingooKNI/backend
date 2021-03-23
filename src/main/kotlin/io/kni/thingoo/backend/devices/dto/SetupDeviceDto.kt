package io.kni.thingoo.backend.devices.dto

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.entities.dto.SetupEntityDto

data class SetupDeviceDto(
    val key: String,
    val macAddress: String,
    val entities: List<SetupEntityDto>
) {
    fun toDevice(): Device {
        return Device(
            id = 0,
            key = key,
            macAddress = macAddress,
            displayName = null,
            icon = null,
            entities = entities.map { it.toEntity() }.toMutableList()
        )
    }
}
