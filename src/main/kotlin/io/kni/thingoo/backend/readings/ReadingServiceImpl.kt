package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.entities.EntityRepository
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
        TODO("Not yet implemented")
    }

    override fun getReadings(entityId: Int): List<ReadingDto> {
        return readingRepository.findByEntityId(entityId).toList().map { it.toDto() }
    }

    override fun getReadings(deviceKey: String, entityKey: String): List<ReadingDto> {
        return readingRepository.findByEntityKeyAndEntityDeviceKey(entityKey, deviceKey).toList().map { it.toDto() }
    }
}
