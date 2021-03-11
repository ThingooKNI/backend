package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto

interface DeviceService {

    fun setupDevice(setupDeviceDto: SetupDeviceDto): DeviceDto

    fun getDevices(): List<DeviceDto>

    fun getDevice(id: Int): DeviceDto

    fun deleteDevice(id: Int)
}
