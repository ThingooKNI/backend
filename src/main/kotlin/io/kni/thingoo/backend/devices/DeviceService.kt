package io.kni.thingoo.backend.devices

import java.util.Optional

interface DeviceService {

    fun registerDevice(registerDeviceDto: RegisterDeviceDto): Device

    fun getAll(): List<Device>

    fun getById(id: Int): Optional<Device>
}
