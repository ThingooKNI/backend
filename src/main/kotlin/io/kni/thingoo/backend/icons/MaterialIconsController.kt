package io.kni.thingoo.backend.icons

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("icons")
class MaterialIconsController(
    private val iconService: MaterialIconService
) {
    @GetMapping
    fun getAllIcons(): ResponseEntity<List<MaterialIcon>> {
        return ResponseEntity.ok(iconService.getIcons())
    }
}
