package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.commands.CommandService
import io.kni.thingoo.backend.commands.dto.NewCommandDto
import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.entities.dto.UpdateEntityDto
import io.kni.thingoo.backend.readings.ReadingService
import io.kni.thingoo.backend.readings.dto.ReadingDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("entities")
class EntityController(
    private val entityService: EntityService,
    private val readingService: ReadingService,
    private val commandsService: CommandService
) {
    @GetMapping("/{id}")
    fun getEntityById(@PathVariable id: Int): ResponseEntity<EntityDto> {
        return ResponseEntity.ok(entityService.getEntityById(id))
    }

    @GetMapping("/{id}/readings")
    fun getAllReadingsForEntity(@PathVariable id: Int): ResponseEntity<List<ReadingDto>> {
        return ResponseEntity.ok(readingService.getReadingsByEntityId(id))
    }

    @GetMapping("/{id}/readings/latest")
    fun getLatestReadingForEntity(@PathVariable id: Int): ResponseEntity<ReadingDto> {
        return ResponseEntity.ok(readingService.getLatestReadingByEntityId(id))
    }

    @PutMapping("/{id}")
    fun updateEntityById(@PathVariable id: Int, @RequestBody updateEntityDto: UpdateEntityDto): ResponseEntity<EntityDto> {
        return ResponseEntity.ok(entityService.updateEntityById(id, updateEntityDto))
    }

    @PatchMapping("/{id}")
    fun patchEntityById(@PathVariable id: Int, @RequestBody patch: Map<String, Any>): ResponseEntity<EntityDto> {
        return ResponseEntity.ok(entityService.patchEntityById(id, patch))
    }

    @PostMapping("/{id}")
    fun postNewCommand(@PathVariable id: Int, @RequestBody command: NewCommandDto) {
        commandsService.sendNewCommandToEntity(command, id)
    }
}
