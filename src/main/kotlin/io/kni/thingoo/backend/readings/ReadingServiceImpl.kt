package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.readings.dto.ReadingDto
import io.kni.thingoo.backend.readings.dto.SaveReadingDto
import org.springframework.stereotype.Service

@Service
class ReadingServiceImpl : ReadingService {

    override fun saveReading(reading: SaveReadingDto): ReadingDto {
        TODO("Not yet implemented")
    }

    override fun getReadings(entityId: Int): List<ReadingDto> {
        TODO("Not yet implemented")
    }

    override fun getReadings(deviceId: String, entityKey: String): List<ReadingDto> {
        TODO("Not yet implemented")
    }
}
