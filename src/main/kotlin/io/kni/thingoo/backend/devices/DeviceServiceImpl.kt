package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.exceptions.ExistingDeviceIDException
import io.kni.thingoo.backend.devices.exceptions.ExistingMACAddressException
import io.kni.thingoo.backend.devices.exceptions.InvalidMACAddressException
import io.kni.thingoo.backend.entities.Entity
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
        validateMacAddressDuplication(registerDeviceDto.macAddress, registerDeviceDto.deviceID)

        validateMacAddress(registerDeviceDto.macAddress)

        validateEntities(registerDeviceDto.entities)

        val existingDeviceOptional = deviceRepository.findByDeviceID(registerDeviceDto.deviceID)

        if (existingDeviceOptional.isPresent) {
            val existingDevice = existingDeviceOptional.get()
            registerExistingDevice(existingDevice, registerDeviceDto)
        } else {
            registerNewDevice(registerDeviceDto)
        }

        return deviceRepository.findByDeviceID(registerDeviceDto.deviceID).get()
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

    private fun validateMacAddressDuplication(macAddress: String, deviceID: String) {
        val device = deviceRepository.findByMacAddress(macAddress)
        if (device.isPresent && device.get().deviceID != deviceID) {
            throw ExistingMACAddressException("There is already a device registered with this macAddress")
        }
    }

    private fun isValidMacAddress(mac: String): Boolean {
        val pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")
        val matcher = pattern.matcher(mac)
        return matcher.find()
    }

    private fun registerNewDevice(registerDeviceDto: RegisterDeviceDto) {
        var device = registerDeviceDto.toDevice()
        device = deviceRepository.save(device)

        val entities = device.entities
        entities.forEach { it.device = device }

        entityRepository.saveAll(entities)
    }

    private fun registerExistingDevice(existingDevice: Device, registerDeviceDto: RegisterDeviceDto) {
        validateDeviceIdCollision(existingDevice, registerDeviceDto)

        val existingEntities = entityRepository.findByDeviceId(existingDevice.id)

        updateNewAndExistingEntities(registerDeviceDto, existingDevice, existingEntities)
        deleteOldEntities(existingEntities, registerDeviceDto)
        updateDevice(existingDevice, registerDeviceDto)

        existingDevice.entities = emptyList() // to prevent saving old entities
        deviceRepository.save(existingDevice)
    }

    private fun validateDeviceIdCollision(existingDevice: Device, registerDeviceDto: RegisterDeviceDto) {
        if (existingDevice.macAddress != registerDeviceDto.macAddress) {
            throw ExistingDeviceIDException("There is already a device registered with this deviceID.")
        }
    }

    private fun updateExistingEntity(entity: RegisterEntityDto, oldEntity: Entity, existingDevice: Device) {
        val updatedEntity = entity.toEntity()
        updatedEntity.id = oldEntity.id
        updatedEntity.device = existingDevice
        entityRepository.save(updatedEntity)
    }

    private fun createNewEntity(entity: RegisterEntityDto, existingDevice: Device) {
        val newEntity = entity.toEntity()
        newEntity.device = existingDevice
        entityRepository.save(newEntity)
    }

    private fun updateNewAndExistingEntities(
        registerDeviceDto: RegisterDeviceDto,
        existingDevice: Device,
        existingEntities: List<Entity>
    ) {
        registerDeviceDto.entities.forEach { entity ->
            val oldEntity = existingEntities.find { it.key == entity.key }
            if (oldEntity != null) {
                updateExistingEntity(entity, oldEntity, existingDevice)
            } else {
                createNewEntity(entity, existingDevice)
            }
        }
    }

    private fun deleteOldEntities(existingEntities: List<Entity>, registerDeviceDto: RegisterDeviceDto) {
        existingEntities.forEach { entity ->
            if (registerDeviceDto.entities.none { it.key == entity.key }) {
                entityRepository.delete(entity)
            }
        }
    }

    private fun updateDevice(existingDevice: Device, registerDeviceDto: RegisterDeviceDto) {
        // Should it be generic? We have only displayName to edit for now but this subset of fields may grow in the future
        existingDevice.displayName = registerDeviceDto.displayName
    }
}
