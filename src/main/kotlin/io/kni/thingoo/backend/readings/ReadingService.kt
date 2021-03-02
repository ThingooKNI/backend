package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.readings.dto.ReadingDto
import io.kni.thingoo.backend.readings.dto.SaveReadingDto

interface ReadingService {

    fun saveReading(reading: SaveReadingDto): ReadingDto

    fun getReadings(entityId: Int): List<ReadingDto>

    fun getReadings(deviceKey: String, entityKey: String): List<ReadingDto>

    fun getLatestReading(entityId: Int): ReadingDto

    fun getLatestReading(deviceKey: String, entityKey: String): ReadingDto
}
