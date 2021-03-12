package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.entities.dto.UpdateEntityDto
import io.kni.thingoo.backend.readings.ReadingService
import io.kni.thingoo.backend.readings.dto.ReadingDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("entities")
class EntityController(
    private val entityService: EntityService,
    private val readingService: ReadingService
) {
    @GetMapping("/{id}")
    fun getEntityById(@PathVariable id: Int): ResponseEntity<EntityDto> {
        return ResponseEntity.ok(entityService.getEntity(id))
    }

    @GetMapping("/{id}/readings")
    fun getReadingsForEntity(@PathVariable id: Int): ResponseEntity<List<ReadingDto>> {
        return ResponseEntity.ok(readingService.getReadings(entityId = id))
    }

    @GetMapping("/{id}/readings/latest")
    fun getLatestReadingForEntity(@PathVariable id: Int): ResponseEntity<ReadingDto> {
        return ResponseEntity.ok(readingService.getLatestReading(entityId = id))
    }

    @PutMapping("/{id}")
    fun updateEntityById(@PathVariable id: Int, @RequestBody updateEntityDto: UpdateEntityDto): ResponseEntity<EntityDto> {
        return ResponseEntity.ok(entityService.updateEntity(updateEntityDto, id))
    }
}
