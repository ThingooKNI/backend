package io.kni.thingoo.backend.integration.device

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.devices.RegisterDeviceDto
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.RegisterEntityDto
import io.kni.thingoo.backend.entities.UnitType
import java.util.Random
import kotlin.experimental.and

fun createTestDevice(id: String = "test", mac: String = randomMACAddress(), name: String = "Test device"): Device {
    return Device(deviceID = id, macAddress = mac, displayName = name)
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
        device = device
    )
}

fun createTestRegisterDeviceDto(
    id: String = "test",
    mac: String = randomMACAddress(),
    name: String = "Test device",
    entities: Set<Entity> = emptySet()
): RegisterDeviceDto {
    return RegisterDeviceDto(
        deviceID = id,
        macAddress = mac,
        displayName = name,
        entities = entities
    )
}

fun createTestRegisterEntityDto(
    key: String = "temp",
    name: String = "Temperature",
    type: EntityType = EntityType.SENSOR,
    unitType: UnitType = UnitType.DECIMAL,
    unitDisplayName: String = "C"
): RegisterEntityDto {
    return RegisterEntityDto(
        key = key,
        displayName = name,
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
