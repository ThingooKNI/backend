package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.UnitType
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

    override fun saveReading(reading: SaveReadingDto): ReadingDto {
        tryGetRelatedDevice(reading)
        val relatedEntity = tryGetRelatedEntity(reading)

        validateReadingValue(reading, relatedEntity.unitType)

        val savedReading = readingRepository.save(
            Reading(
                value = reading.value,
                entity = relatedEntity
            )
        )

        return savedReading.toDto()
    }

    override fun getReadings(entityId: Int): List<ReadingDto> {
        return readingRepository.findByEntityId(entityId).toList().map { it.toDto() }
    }

    override fun getReadings(deviceKey: String, entityKey: String): List<ReadingDto> {
        return readingRepository.findByEntityKeyAndEntityDeviceKey(entityKey, deviceKey).toList().map { it.toDto() }
    }

    override fun getLatestReading(entityId: Int): ReadingDto {
        return readingRepository.findFirstByEntityIdOrderByTimestampDesc(entityId).map { it.toDto() }.orElseThrow {
            ApiErrorCode.READINGS_002.throwException()
        }
    }

    override fun getLatestReading(deviceKey: String, entityKey: String): ReadingDto {
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

    private fun validateReadingValue(reading: SaveReadingDto, valueType: UnitType) {
        if (!valueType.getReadingValueValidator().isValid(reading.value)) {
            ApiErrorCode.READINGS_001.throwException()
        }
    }
}
