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
    @GetMapping
    fun getAllReadingsByDeviceKeyAndEntityKey(
        @RequestParam("device_key") deviceKey: String,
        @RequestParam("entity_key") entityKey: String
    ): ResponseEntity<List<ReadingDto>> {
        return ResponseEntity.ok(readingService.getReadingsByDeviceKeyAndEntityKey(deviceKey, entityKey))
    }

    @GetMapping("/latest")
    fun getLatestReadingByDeviceKeyAndEntityKey(
        @RequestParam("device_key") deviceKey: String,
        @RequestParam("entity_key") entityKey: String
    ): ResponseEntity<ReadingDto> {
        return ResponseEntity.ok(readingService.getLatestReadingByDeviceKeyAndEntityKey(deviceKey, entityKey))
    }

    @PostMapping
    fun createNewReading(@RequestBody saveReadingDto: SaveReadingDto): ResponseEntity<ReadingDto> {
        return ResponseEntity.ok(readingService.createReading(saveReadingDto))
    }
}
