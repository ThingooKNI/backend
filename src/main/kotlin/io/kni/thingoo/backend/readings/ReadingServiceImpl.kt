package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.ValueType
import io.kni.thingoo.backend.exceptions.ApiErrorCode
import io.kni.thingoo.backend.readings.dto.ReadingDto
import io.kni.thingoo.backend.readings.dto.SaveReadingDto
import org.springframework.stereotype.Service

@Service
class ReadingServiceImpl(
    private val readingRepository: ReadingRepository,
    private val deviceRepository: DeviceRepository,
    private val entityRepository: EntityRepository
) : ReadingService {

    override fun createReading(reading: SaveReadingDto): ReadingDto {
        tryGetRelatedDevice(reading)
        val relatedEntity = tryGetRelatedEntity(reading)

        validateReadingValue(reading, relatedEntity.valueType)

        val savedReading = readingRepository.save(
            Reading(
                value = reading.value,
                entity = relatedEntity
            )
        )

        return savedReading.toDto()
    }

    override fun getReadingsByEntityId(entityId: Int): List<ReadingDto> {
        return readingRepository.findByEntityId(entityId).toList().map { it.toDto() }
    }

    override fun getReadingsByDeviceKeyAndEntityKey(deviceKey: String, entityKey: String): List<ReadingDto> {
        return readingRepository.findByEntityKeyAndEntityDeviceKey(entityKey, deviceKey).toList().map { it.toDto() }
    }

    override fun getLatestReadingByEntityId(entityId: Int): ReadingDto {
        return readingRepository.findFirstByEntityIdOrderByTimestampDesc(entityId).map { it.toDto() }.orElseThrow {
            ApiErrorCode.READINGS_002.throwException()
        }
    }

    override fun getLatestReadingByDeviceKeyAndEntityKey(deviceKey: String, entityKey: String): ReadingDto {
        return readingRepository.findFirstByEntityKeyAndEntityDeviceKeyOrderByTimestampDesc(entityKey, deviceKey).map { it.toDto() }.orElseThrow {
            ApiErrorCode.READINGS_002.throwException()
        }
    }

    private fun tryGetRelatedDevice(reading: SaveReadingDto): Device {
        val relatedDeviceOptional = deviceRepository.findByKey(reading.deviceKey)
        return relatedDeviceOptional.orElseThrow { ApiErrorCode.DEVICES_005.throwException() }
    }

    private fun tryGetRelatedEntity(reading: SaveReadingDto): Entity {
        val relatedEntityOptional = entityRepository.findByKeyAndDeviceKey(reading.entityKey, reading.deviceKey)
        return relatedEntityOptional.orElseThrow { ApiErrorCode.ENTITIES_002.throwException() }
    }

    private fun validateReadingValue(reading: SaveReadingDto, valueType: ValueType) {
        if (!valueType.getValueValidator().isValid(reading.value)) {
            ApiErrorCode.READINGS_001.throwException()
        }
    }
}
