package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.RegisterDeviceDto
import io.kni.thingoo.backend.entities.dto.EntityDto

interface DeviceService {

    fun registerDevice(registerDeviceDto: RegisterDeviceDto): DeviceDto

    fun getDevices(): List<DeviceDto>

    fun getDevice(id: Int): DeviceDto

    fun deleteDevice(id: Int)
}
