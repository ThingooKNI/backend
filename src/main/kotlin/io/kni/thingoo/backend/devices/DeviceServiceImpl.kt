package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.exceptions.ExistingDeviceIDException
import io.kni.thingoo.backend.devices.exceptions.InvalidMACAddressException
import io.kni.thingoo.backend.entities.EntityRepository
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

    @Autowired
    private lateinit var entityRepository: EntityRepository

    override fun registerDevice(registerDeviceDto: RegisterDeviceDto): Device {
        // TODO validate whether this macAddress has at most 1 device (mac cannot have 2 devices), add test

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
        var device = registerDeviceDto.toDevice()
        device = deviceRepository.save(device)

        val entities = device.entities
        entities.forEach { it.device = device }

        entityRepository.saveAll(entities)
        return device
    }

    private fun registerExistingDevice(existingDevice: Device, registerDeviceDto: RegisterDeviceDto): Device {
        if (existingDevice.macAddress != registerDeviceDto.macAddress) {
            throw ExistingDeviceIDException("There is already a device registered with this deviceID.")
        }

        // TODO REFACTOR
        val existingEntities = entityRepository.findByDeviceId(existingDevice.id)

        registerDeviceDto.entities.forEach { entity ->
            val oldEntity = existingEntities.find { it.key == entity.key }
            if (oldEntity != null) {
                val updatedEntity = entity.toEntity()
                updatedEntity.id = oldEntity.id
                updatedEntity.device = existingDevice
                entityRepository.save(updatedEntity)
            } else {
                val newEntity = entity.toEntity()
                newEntity.device = existingDevice
                entityRepository.save(newEntity)
            }
        }

        existingEntities.forEach { entity ->
            if (registerDeviceDto.entities.none { it.key == entity.key }) {
                entityRepository.delete(entity)
            }
        }

        // update device data
        // Should it be generic? We have only displayName to edit for now but this subset of fields may grow in the future
        existingDevice.displayName = registerDeviceDto.displayName

        existingDevice.entities = emptyList() // to prevent cascading PERSIST of old entities
        return deviceRepository.save(existingDevice)
    }
}
