/* ktlint-disable max-line-length */
package io.kni.thingoo.backend.integration.devices

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.devices.exceptions.DeviceNotFoundException
import io.kni.thingoo.backend.devices.exceptions.ExistingDeviceKeyException
import io.kni.thingoo.backend.devices.exceptions.ExistingMACAddressException
import io.kni.thingoo.backend.devices.exceptions.InvalidMACAddressException
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType
import io.kni.thingoo.backend.entities.exceptions.ExistingEntityKeyException
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class DeviceServiceTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Autowired
    private lateinit var deviceService: DeviceService

    @AfterEach
    fun clear() {
        deviceRepository.deleteAll()
        entityRepository.deleteAll()
    }

    @Test
    fun `given new device, when registering new device with invalid mac, then will throw InvalidMACAddressException`() {
        // given
        val newDevice = createTestRegisterDeviceDto(mac = "test")

        // when

        // then
        assertThrows<InvalidMACAddressException> { deviceService.registerDevice(newDevice) }
    }

    @Test
    fun `given new device, when registering new device, then will register new device with entities`() {
        // given
        val newDevice = createTestRegisterDeviceDto()
        newDevice.entities = listOf(
            createTestRegisterEntityDto(key = "1"),
            createTestRegisterEntityDto(key = "2")
        )

        // when
        deviceService.registerDevice(newDevice)

        // then
        val createdDeviceOptional = deviceRepository.findByKey(newDevice.key)
        assertThat(createdDeviceOptional.isPresent).isTrue
        val createdDevice = createdDeviceOptional.get()
        assertThat(createdDevice.id).isNotEqualTo(0)
        assertThat(createdDevice.entities).hasSize(2)
    }

    @Test
    fun `given existing device, when registering new device with same key and same mac address, then will register one`() {
        // given
        val existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestRegisterDeviceDto(id = "device1", mac = "00:A0:C9:14:C8:29")
        deviceService.registerDevice(newDevice)

        // then
        val createdDeviceOptional = deviceRepository.findByKey(newDevice.key)
        assertThat(createdDeviceOptional.isPresent).isTrue
    }

    @Test
    fun `given existing device, when registering new device with same key and different mac address, then will throw ExistingDeviceKeyException`() {
        // given
        val existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestRegisterDeviceDto(id = "device1", mac = "00:A5:D3:26:C2:37")

        // then
        assertThrows<ExistingDeviceKeyException> { deviceService.registerDevice(newDevice) }
    }

    @Test
    fun `given new device, when registering new device with duplicated entities, then will throw`() {
        // given
        val newDevice = createTestRegisterDeviceDto()
        newDevice.entities = listOf(
            createTestRegisterEntityDto(key = "1"),
            createTestRegisterEntityDto(key = "1")
        )

        // when

        // then
        assertThrows<ExistingEntityKeyException> { deviceService.registerDevice(newDevice) }
    }

    @Test
    fun `given existing device, when registering same device and different entities, then will update entities`() {
        // given
        var existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29", name = "device1")
        existingDevice = deviceRepository.save(existingDevice)

        val existingEntities = listOf(
            createTestEntity(
                key = "temp",
                name = "temperature",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
                device = existingDevice
            ),
            createTestEntity(
                key = "hum",
                name = "humidity",
                type = EntityType.SENSOR,
                unitType = UnitType.INTEGER,
                unitDisplayName = "%",
                device = existingDevice
            )
        )
        entityRepository.saveAll(existingEntities)
        existingDevice = deviceRepository.findById(existingDevice.id).get()

        // when
        val newDevice = existingDevice.toRegisterDeviceDto()
        val newEntities = listOf(
            createTestRegisterEntityDto(
                key = "temp",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "F"
            ),
            createTestRegisterEntityDto(
                key = "hum",
                type = EntityType.SENSOR,
                unitType = UnitType.INTEGER,
                unitDisplayName = "%"
            ),
            createTestRegisterEntityDto(
                key = "online",
                type = EntityType.SENSOR,
                unitType = UnitType.BOOLEAN,
                unitDisplayName = ""
            )
        )
        newDevice.entities = newEntities

        deviceService.registerDevice(newDevice)

        // then
        val updatedDevice = deviceRepository.findByKey(newDevice.key).get()
        assertThat(updatedDevice.entities).hasSize(3)
        assertThat(updatedDevice.entities.any { it.unitDisplayName == "F" })
    }

    @Test
    fun `given existing device, when registering different device and same entities, then will update device`() {
        // given
        var existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29", name = "device1")
        existingDevice = deviceRepository.save(existingDevice)

        val existingEntities = listOf(
            createTestEntity(
                key = "temp",
                name = "temperature",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
                device = existingDevice
            ),
            createTestEntity(
                key = "hum",
                name = "humidity",
                type = EntityType.SENSOR,
                unitType = UnitType.INTEGER,
                unitDisplayName = "%",
                device = existingDevice
            )
        )
        entityRepository.saveAll(existingEntities)
        existingDevice = deviceRepository.findById(existingDevice.id).get()

        // when
        val newDevice = existingDevice.toRegisterDeviceDto()

        deviceService.registerDevice(newDevice)

        // then
        val updatedDevice = deviceRepository.findByKey(newDevice.key).get()
        assertThat(updatedDevice.entities).hasSize(2)
        assertThat(updatedDevice.displayName).isEqualTo("device1")
    }

    @Test
    fun `given existing device, when registering same device with entity deleted, then will delete entity`() {
        // given
        var existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29", name = "device1")
        existingDevice = deviceRepository.save(existingDevice)

        val existingEntities = listOf(
            createTestEntity(
                key = "temp",
                name = "temperature",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
                device = existingDevice
            ),
            createTestEntity(
                key = "hum",
                name = "humidity",
                type = EntityType.SENSOR,
                unitType = UnitType.INTEGER,
                unitDisplayName = "%",
                device = existingDevice
            )
        )
        entityRepository.saveAll(existingEntities)
        existingDevice = deviceRepository.findById(existingDevice.id).get()

        // when
        val newDevice = existingDevice.toRegisterDeviceDto()
        val newEntities = listOf(
            createTestRegisterEntityDto(
                key = "temp",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
            ),
        )
        newDevice.entities = newEntities

        deviceService.registerDevice(newDevice)

        // then
        val updatedDevice = deviceRepository.findByKey(newDevice.key).get()
        assertThat(updatedDevice.entities).hasSize(1)
    }

    @Test
    fun `given existing device, when registering new device with same macAddress and different key, then will throw ExistingMACAddressException`() {
        // given
        val existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestRegisterDeviceDto(id = "device2", mac = "00:A0:C9:14:C8:29")

        // then
        assertThrows<ExistingMACAddressException> { deviceService.registerDevice(newDevice) }
    }

    @Test
    fun `given new device, when getting all devices, then will be one device`() {
        // given
        val newDevice = createTestDevice()
        deviceRepository.save(newDevice)

        // when
        val devices = deviceService.getDevices()

        // then
        assertThat(devices).hasSize(1)
    }

    @Test
    fun `given new device, when getting device by id, then will return one`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val device = deviceService.getDevice(savedDevice.id)

        // then
        assertThat(device).isNotNull
    }

    @Test
    fun `given no device, when getting device by id, then will throw DeviceNotFoundException`() {
        // given

        // when
        assertThrows<DeviceNotFoundException> { deviceService.getDevice(999) }

        // then
    }

    @Test
    fun `given existing device, when deleteing device by id, then will delete one`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        deviceService.deleteDevice(savedDevice.id)

        // then
        assertThat(deviceRepository.findAll()).isEmpty()
        assertThat(entityRepository.findByDeviceId(savedDevice.id)).isEmpty()
    }
}
