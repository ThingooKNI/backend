package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.PatchDeviceDto
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto
import io.kni.thingoo.backend.devices.dto.UpdateDeviceDto
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.dto.SetupEntityDto
import io.kni.thingoo.backend.exceptions.ApiErrorCode
import io.kni.thingoo.backend.utils.PatchUtils
import io.kni.thingoo.backend.utils.exception.EntityPatchException
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class DeviceServiceImpl(
    private val deviceRepository: DeviceRepository,
    private val entityRepository: EntityRepository
) : DeviceService {

    override fun setupDevice(setupDeviceDto: SetupDeviceDto): DeviceDto {
        validateMacAddressDuplication(setupDeviceDto.macAddress, setupDeviceDto.key)

        validateMacAddress(setupDeviceDto.macAddress)

        validateEntities(setupDeviceDto.entities)

        val existingDeviceOptional = deviceRepository.findByKey(setupDeviceDto.key)

        if (existingDeviceOptional.isPresent) {
            val existingDevice = existingDeviceOptional.get()
            setupExistingDevice(existingDevice, setupDeviceDto)
        } else {
            setupNewDevice(setupDeviceDto)
        }

        return deviceRepository.findByKey(setupDeviceDto.key).get().toDto()
    }

    override fun getAllDevices(): List<DeviceDto> {
        return deviceRepository.findAll().toList().map { it.toDto() }
    }

    override fun getDeviceById(id: Int): DeviceDto {
        val deviceOptional = deviceRepository.findById(id)
        return deviceOptional
            .map { it.toDto() }
            .orElseThrow {
                ApiErrorCode.DEVICES_001.throwException()
            }
    }

    override fun deleteDeviceById(id: Int) {
        val device = deviceRepository.findById(id)
        if (device.isEmpty) {
            ApiErrorCode.DEVICES_001.throwException()
        }

        deviceRepository.deleteById(id)
    }

    override fun updateDeviceById(id: Int, updateDeviceDto: UpdateDeviceDto): DeviceDto {
        val deviceOptional = deviceRepository.findById(id)
        if (deviceOptional.isEmpty) {
            ApiErrorCode.DEVICES_001.throwException()
        }

        val device = deviceOptional.get()
        device.displayName = updateDeviceDto.displayName
        device.icon = updateDeviceDto.icon
        return deviceRepository.save(device).toDto()
    }

    override fun patchDeviceById(id: Int, patch: Map<String, Any>): DeviceDto {
        val deviceOptional = deviceRepository.findById(id)
        if (deviceOptional.isEmpty) {
            ApiErrorCode.DEVICES_001.throwException()
        }

        val device = deviceOptional.get()
        try {
            PatchUtils.patchEntity(patch, PatchDeviceDto::class, device)
        } catch (e: EntityPatchException) {
            ApiErrorCode.DEVICES_006.throwException()
        }

        return deviceRepository.save(device).toDto()
    }

    private fun validateEntities(entities: List<SetupEntityDto>) {
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

    private fun setupNewDevice(setupDeviceDto: SetupDeviceDto) {
        var device = setupDeviceDto.toDevice()
        device = deviceRepository.save(device)

        val entities = device.entities
        entities.forEach { it.device = device }

        entityRepository.saveAll(entities)
    }

    private fun setupExistingDevice(existingDevice: Device, setupDeviceDto: SetupDeviceDto) {
        validateDeviceKeyCollision(existingDevice, setupDeviceDto)

        val existingEntities = entityRepository.findByDeviceId(existingDevice.id)

        updateNewAndExistingEntities(setupDeviceDto, existingDevice, existingEntities)
        deleteOldEntities(existingEntities, setupDeviceDto, existingDevice)
    }

    private fun validateDeviceKeyCollision(existingDevice: Device, setupDeviceDto: SetupDeviceDto) {
        if (existingDevice.macAddress != setupDeviceDto.macAddress) {
            ApiErrorCode.DEVICES_004.throwException()
        }
    }

    private fun updateNewAndExistingEntities(
        setupDeviceDto: SetupDeviceDto,
        existingDevice: Device,
        existingEntities: List<Entity>
    ) {
        setupDeviceDto.entities.forEach { entity ->
            val oldEntity = existingEntities.find { it.key == entity.key }
            if (oldEntity != null) {
                updateExistingEntity(entity, oldEntity, existingDevice)
            } else {
                createNewEntity(entity, existingDevice)
            }
        }
    }

    private fun updateExistingEntity(entity: SetupEntityDto, oldEntity: Entity, existingDevice: Device) {
        val updatedEntity = entity.toEntity()
        updatedEntity.id = oldEntity.id
        updatedEntity.device = existingDevice
        entityRepository.save(updatedEntity)
    }

    private fun createNewEntity(entity: SetupEntityDto, existingDevice: Device) {
        val newEntity = entity.toEntity()
        existingDevice.addEntity(newEntity)
        entityRepository.save(newEntity)
    }

    private fun deleteOldEntities(existingEntities: List<Entity>, setupDeviceDto: SetupDeviceDto, existingDevice: Device) {
        existingEntities.forEach { entity ->
            if (setupDeviceDto.entities.none { it.key == entity.key }) {
                existingDevice.deleteEntity(entity)
                entityRepository.delete(entity)
            }
        }
    }
}
