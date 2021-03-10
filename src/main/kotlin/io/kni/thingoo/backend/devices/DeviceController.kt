package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto
import io.kni.thingoo.backend.entities.EntityService
import io.kni.thingoo.backend.entities.dto.EntityDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("devices")
class DeviceController(
    private val deviceService: DeviceService,
    private val entityService: EntityService
) {
    @GetMapping
    fun getAllDevices(): ResponseEntity<List<DeviceDto>> {
        return ResponseEntity.ok(deviceService.getDevices())
    }

    @GetMapping("/{id}")
    fun getDeviceById(@PathVariable id: Int): ResponseEntity<DeviceDto> {
        return ResponseEntity.ok(deviceService.getDevice(id))
    }

    @GetMapping("/{id}/entities")
    fun getEntitiesForDevice(@PathVariable id: Int): ResponseEntity<List<EntityDto>> {
        return ResponseEntity.ok(entityService.getEntities(deviceId = id))
    }

    @PostMapping
    fun setupDevice(@RequestBody setupDeviceDto: SetupDeviceDto): ResponseEntity<DeviceDto> {
        return ResponseEntity.ok(deviceService.setupDevice(setupDeviceDto))
    }

    @DeleteMapping("/{id}")
    fun deleteDeviceById(@PathVariable id: Int) {
        deviceService.deleteDevice(id)
    }
}
