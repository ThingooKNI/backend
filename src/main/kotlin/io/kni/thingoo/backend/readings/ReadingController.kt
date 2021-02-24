package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.readings.dto.ReadingDto
import io.kni.thingoo.backend.readings.dto.SaveReadingDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("readings")
class ReadingController(
    private val readingService: ReadingService
) {
    @GetMapping(params = ["entity_id"])
    fun getReadings(@RequestParam("entity_id") entityId: Int): ResponseEntity<List<ReadingDto>> {
        return ResponseEntity.ok(readingService.getReadings(entityId))
    }

    @GetMapping(params = ["device_id", "entity_key"])
    fun getReadings(@RequestParam("device_id") deviceId: String, @RequestParam("entity_key") entityKey: String): ResponseEntity<List<ReadingDto>> {
        return ResponseEntity.ok(readingService.getReadings(deviceId, entityKey))
    }

    @PostMapping
    fun saveReading(@RequestBody saveReadingDto: SaveReadingDto): ResponseEntity<ReadingDto> {
        return ResponseEntity.ok(readingService.saveReading(saveReadingDto))
    }
}
