package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.readings.dto.ReadingDto
import io.kni.thingoo.backend.readings.dto.SaveReadingDto

interface ReadingService {

    fun createReading(reading: SaveReadingDto): ReadingDto

    fun getReadingsByEntityId(entityId: Int): List<ReadingDto>

    fun getReadingsByDeviceKeyAndEntityKey(deviceKey: String, entityKey: String): List<ReadingDto>

    fun getLatestReadingByEntityId(entityId: Int): ReadingDto

    fun getLatestReadingByDeviceKeyAndEntityKey(deviceKey: String, entityKey: String): ReadingDto
}
