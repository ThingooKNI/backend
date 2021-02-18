package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.RegisterDeviceDto
import java.util.Optional

interface DeviceService {

    fun registerDevice(registerDeviceDto: RegisterDeviceDto): DeviceDto

    fun getAll(): List<DeviceDto>

    fun getById(id: Int): DeviceDto
}
