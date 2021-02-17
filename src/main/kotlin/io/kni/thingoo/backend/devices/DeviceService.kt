package io.kni.thingoo.backend.devices

import org.springframework.stereotype.Service
import java.util.Optional

@Service
interface DeviceService {

    fun registerDevice(registerDeviceDto: RegisterDeviceDto): Device

    fun getAll(): List<Device>

    fun getById(id: Int): Optional<Device>
}
