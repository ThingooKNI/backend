package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.exceptions.ExistingDeviceIDException
import io.kni.thingoo.backend.devices.exceptions.InvalidMACAddressException
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.RegisterEntityDto
import io.kni.thingoo.backend.entities.exceptions.ExistingEntityKeyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.regex.Pattern

@Service
class DeviceServiceImpl : DeviceService {
    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    override fun registerDevice(registerDeviceDto: RegisterDeviceDto): Device {
        validateMacAddress(registerDeviceDto.macAddress)

        validateEntities(registerDeviceDto.entities)

        val existingDeviceOptional = deviceRepository.findByDeviceID(registerDeviceDto.deviceID)

        return if (existingDeviceOptional.isPresent) {
            val existingDevice = existingDeviceOptional.get()
            registerExistingDevice(existingDevice, registerDeviceDto)
        } else {
            registerNewDevice(registerDeviceDto)
        }
    }

    override fun getAll(): List<Device> {
        return deviceRepository.findAll().toList()
    }

    override fun getById(id: Int): Optional<Device> {
        return deviceRepository.findById(id)
    }

    private fun validateEntities(entities: List<RegisterEntityDto>) {
        if (entities.distinctBy { it.key }.size != entities.size) {
            throw ExistingEntityKeyException("Duplicated Entity key value")
        }
    }

    private fun validateMacAddress(mac: String) {
        if (!isValidMacAddress(mac)) {
            throw InvalidMACAddressException("Invalid mac address: $mac")
        }
    }

    private fun isValidMacAddress(mac: String): Boolean {
        val pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")
        val matcher = pattern.matcher(mac)
        return matcher.find()
    }

    private fun registerNewDevice(registerDeviceDto: RegisterDeviceDto): Device {
        val device = registerDeviceDto.toDevice()
        device.entities.forEach { it.device = device }
        println(device)
        return deviceRepository.save(device)
    }

    private fun registerExistingDevice(existingDevice: Device, registerDeviceDto: RegisterDeviceDto): Device {
        if (existingDevice.macAddress != registerDeviceDto.macAddress) {
            throw ExistingDeviceIDException("There is already a device registered with this deviceID.")
        }
        // If existing device, overwrite entities and device info

        TODO()
    }
}
