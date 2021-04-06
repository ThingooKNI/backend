package io.kni.thingoo.backend.utils

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType
import io.kni.thingoo.backend.entities.dto.SetupEntityDto
import java.util.Random
import kotlin.experimental.and

fun createTestDevice(key: String = "test", mac: String = randomMACAddress(), name: String = "Test device"): Device {
    return Device(key = key, macAddress = mac, displayName = name, icon = null)
}

fun createTestEntity(
    key: String = "temp",
    name: String = "Temperature",
    type: EntityType = EntityType.SENSOR,
    unitType: UnitType = UnitType.DECIMAL,
    unitDisplayName: String = "C",
    device: Device? = null
): Entity {
    return Entity(
        key = key,
        displayName = name,
        type = type,
        unitType = unitType,
        unitDisplayName = unitDisplayName,
        device = device,
        icon = null
    )
}

fun createTestsSetupDeviceDto(
    id: String = "test",
    mac: String = randomMACAddress(),
    entities: List<SetupEntityDto> = emptyList()
): SetupDeviceDto {
    return SetupDeviceDto(
        key = id,
        macAddress = mac,
        entities = entities
    )
}

fun createTestSetupEntityDto(
    key: String = "temp",
    type: EntityType = EntityType.SENSOR,
    unitType: UnitType = UnitType.DECIMAL,
    unitDisplayName: String = "C"
): SetupEntityDto {
    return SetupEntityDto(
        key = key,
        type = type,
        unitType = unitType,
        unitDisplayName = unitDisplayName
    )
}

private fun randomMACAddress(): String {
    val rand = Random()
    val macAddr = ByteArray(6)
    rand.nextBytes(macAddr)
    macAddr[0] =
        (macAddr[0] and 254.toByte())
    val sb = StringBuilder(18)
    for (b in macAddr) {
        if (sb.isNotEmpty()) sb.append(":")
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}

fun saveDevice(deviceRepository: DeviceRepository, device: Device): Device {
    return deviceRepository.save(device)
}

fun saveEntities(entityRepository: EntityRepository, device: Device, entities: List<Entity>): List<Entity> {
    return entityRepository.saveAll(entities.map { getEntityForDevice(it, device) }).toList()
}

fun getEntityForDevice(entity: Entity, device: Device): Entity {
    // copy entity object to prevent mutating reference
    return Entity(entity.id, entity.key, entity.displayName, entity.type, entity.unitType, entity.unitDisplayName, null, device)
}
