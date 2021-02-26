package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.exceptions.DeviceNotFoundException
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.UnitType
import io.kni.thingoo.backend.entities.exceptions.EntityNotFoundException
import io.kni.thingoo.backend.readings.dto.ReadingDto
import io.kni.thingoo.backend.readings.dto.SaveReadingDto
import io.kni.thingoo.backend.readings.exceptions.ReadingUnitTypeMismatchException
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

    private fun tryGetRelatedDevice(reading: SaveReadingDto): Device {
        val relatedDeviceOptional = deviceRepository.findByKey(reading.deviceKey)
        return relatedDeviceOptional.orElseThrow { DeviceNotFoundException("Device with given key doesn't exist") }
    }

    private fun tryGetRelatedEntity(reading: SaveReadingDto): Entity {
        val relatedEntityOptional = entityRepository.findByKeyAndDeviceKey(reading.entityKey, reading.deviceKey)
        return relatedEntityOptional.orElseThrow { EntityNotFoundException("Entity with given key and deviceKey doesn't exist") }
    }

    private fun validateReadingValue(reading: SaveReadingDto, valueType: UnitType) {
        if (!valueType.getReadingValueValidator().isValid(reading.value)) {
            throw ReadingUnitTypeMismatchException("${reading.value} is not correct value of $valueType type")
        }
    }
}
