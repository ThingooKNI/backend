package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto
import io.kni.thingoo.backend.devices.dto.UpdateDeviceDto

interface DeviceService {

    fun setupDevice(setupDeviceDto: SetupDeviceDto): DeviceDto

    fun getDevices(): List<DeviceDto>

    fun getDevice(id: Int): DeviceDto

    fun deleteDevice(id: Int)

    fun updateDevice(updateDeviceDto: UpdateDeviceDto, id: Int): DeviceDto
}
