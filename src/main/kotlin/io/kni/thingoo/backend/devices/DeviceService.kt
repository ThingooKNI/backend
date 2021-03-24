package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto
import io.kni.thingoo.backend.devices.dto.UpdateDeviceDto

interface DeviceService {

    fun setupDevice(setupDeviceDto: SetupDeviceDto): DeviceDto

    fun getAllDevices(): List<DeviceDto>

    fun getDeviceById(id: Int): DeviceDto

    fun deleteDeviceById(id: Int)

    fun updateDeviceById(id: Int, updateDeviceDto: UpdateDeviceDto): DeviceDto

    fun patchDeviceById(id: Int, patch: Map<String, Any>): DeviceDto
}
