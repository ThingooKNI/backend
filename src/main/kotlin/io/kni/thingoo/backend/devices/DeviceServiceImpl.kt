package io.kni.thingoo.backend.devices

import org.springframework.beans.factory.annotation.Autowired
import java.util.Optional

class DeviceServiceImpl : DeviceService {
    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    override fun registerDevice(registerDeviceDto: RegisterDeviceDto): Device {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Device> {
        return deviceRepository.findAll().toList()
    }

    override fun getById(id: Int): Optional<Device> {
        return deviceRepository.findById(id)
    }
}
