package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.RegisterDeviceDto
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.dto.RegisterEntityDto
import io.kni.thingoo.backend.exceptions.ApiErrorCode
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class DeviceServiceImpl(
    private val deviceRepository: DeviceRepository,
    private val entityRepository: EntityRepository
) : DeviceService {

    override fun registerDevice(registerDeviceDto: RegisterDeviceDto): DeviceDto {
        validateMacAddressDuplication(registerDeviceDto.macAddress, registerDeviceDto.key)

        validateMacAddress(registerDeviceDto.macAddress)

        validateEntities(registerDeviceDto.entities)

        val existingDeviceOptional = deviceRepository.findByKey(registerDeviceDto.key)

        if (existingDeviceOptional.isPresent) {
            val existingDevice = existingDeviceOptional.get()
            registerExistingDevice(existingDevice, registerDeviceDto)
        } else {
            registerNewDevice(registerDeviceDto)
        }

        return deviceRepository.findByKey(registerDeviceDto.key).get().toDto()
    }

    override fun getDevices(): List<DeviceDto> {
        return deviceRepository.findAll().toList().map { it.toDto() }
    }

    override fun getDevice(id: Int): DeviceDto {
        val deviceOptional = deviceRepository.findById(id)
        return deviceOptional
            .map { it.toDto() }
            .orElseThrow {
                ApiErrorCode.DEVICES_001.throwException()
            }
    }

    override fun deleteDevice(id: Int) {
        val device = deviceRepository.findById(id)
        if (device.isEmpty) {
            ApiErrorCode.DEVICES_001.throwException()
        }

        deviceRepository.deleteById(id)
    }

    private fun validateEntities(entities: List<RegisterEntityDto>) {
        if (entities.distinctBy { it.key }.size != entities.size) {
            ApiErrorCode.ENTITIES_001.throwException()
        }
    }

    private fun validateMacAddress(mac: String) {
        if (!isValidMacAddress(mac)) {
            ApiErrorCode.DEVICES_002.throwException()
        }
    }

    private fun validateMacAddressDuplication(macAddress: String, key: String) {
        val device = deviceRepository.findByMacAddress(macAddress)
        if (device.isPresent && device.get().key != key) {
            ApiErrorCode.DEVICES_003.throwException()
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
        validateDeviceKeyCollision(existingDevice, registerDeviceDto)

        updateDevice(existingDevice, registerDeviceDto)

        val existingEntities = entityRepository.findByDeviceId(existingDevice.id)

        updateNewAndExistingEntities(registerDeviceDto, existingDevice, existingEntities)
        deleteOldEntities(existingEntities, registerDeviceDto, existingDevice)
    }

    private fun validateDeviceKeyCollision(existingDevice: Device, registerDeviceDto: RegisterDeviceDto) {
        if (existingDevice.macAddress != registerDeviceDto.macAddress) {
            ApiErrorCode.DEVICES_004.throwException()
        }
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

    private fun updateExistingEntity(entity: RegisterEntityDto, oldEntity: Entity, existingDevice: Device) {
        val updatedEntity = entity.toEntity()
        updatedEntity.id = oldEntity.id
        updatedEntity.device = existingDevice
        entityRepository.save(updatedEntity)
    }

    private fun createNewEntity(entity: RegisterEntityDto, existingDevice: Device) {
        val newEntity = entity.toEntity()
        existingDevice.addEntity(newEntity)
        entityRepository.save(newEntity)
    }

    private fun deleteOldEntities(existingEntities: List<Entity>, registerDeviceDto: RegisterDeviceDto, existingDevice: Device) {
        existingEntities.forEach { entity ->
            if (registerDeviceDto.entities.none { it.key == entity.key }) {
                existingDevice.deleteEntity(entity)
                entityRepository.delete(entity)
            }
        }
    }

    private fun updateDevice(existingDevice: Device, registerDeviceDto: RegisterDeviceDto) {
        // Should it be generic? We have only displayName to edit for now but this subset of fields may grow in the future
        existingDevice.displayName = registerDeviceDto.displayName
        deviceRepository.save(existingDevice)
    }
}
