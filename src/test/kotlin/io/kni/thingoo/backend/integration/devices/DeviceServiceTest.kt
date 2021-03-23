/* ktlint-disable max-line-length */
package io.kni.thingoo.backend.integration.devices

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.devices.dto.UpdateDeviceDto
import io.kni.thingoo.backend.devices.exceptions.DeviceNotFoundException
import io.kni.thingoo.backend.devices.exceptions.ExistingDeviceKeyException
import io.kni.thingoo.backend.devices.exceptions.ExistingMACAddressException
import io.kni.thingoo.backend.devices.exceptions.InvalidDevicePatchEntryValueException
import io.kni.thingoo.backend.devices.exceptions.InvalidMACAddressException
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType
import io.kni.thingoo.backend.entities.exceptions.ExistingEntityKeyException
import io.kni.thingoo.backend.icons.MaterialIcon
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

    val displayNameKey = "displayName"
    val testNewDisplayName = "new device"

    @Test
    fun `given new device, when setting up new device with invalid mac, then will throw InvalidMACAddressException`() {
        // given
        val newDevice = createTestsSetupDeviceDto(mac = "test")

        // when

        // then
        assertThrows<InvalidMACAddressException> { deviceService.setupDevice(newDevice) }
    }

    @Test
    fun `given new device, when setting up new device, then will setup new device with entities`() {
        // given
        val newDevice = createTestsSetupDeviceDto(
            entities = listOf(
                createTestSetupEntityDto(key = "1"),
                createTestSetupEntityDto(key = "2")
            )
        )

        // when
        deviceService.setupDevice(newDevice)

        // then
        val createdDeviceOptional = deviceRepository.findByKey(newDevice.key)
        assertThat(createdDeviceOptional.isPresent).isTrue
        val createdDevice = createdDeviceOptional.get()
        assertThat(createdDevice.id).isNotEqualTo(0)
        assertThat(createdDevice.entities).hasSize(2)
    }

    @Test
    fun `given existing device, when setting up new device with same key and same mac address, then will setup one`() {
        // given
        val existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestsSetupDeviceDto(id = "device1", mac = "00:A0:C9:14:C8:29")
        deviceService.setupDevice(newDevice)

        // then
        val createdDeviceOptional = deviceRepository.findByKey(newDevice.key)
        assertThat(createdDeviceOptional.isPresent).isTrue
    }

    @Test
    fun `given existing device, when setting up new device with same key and different mac address, then will throw ExistingDeviceKeyException`() {
        // given
        val existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestsSetupDeviceDto(id = "device1", mac = "00:A5:D3:26:C2:37")

        // then
        assertThrows<ExistingDeviceKeyException> { deviceService.setupDevice(newDevice) }
    }

    @Test
    fun `given new device, when setting up new device with duplicated entities, then will throw`() {
        // given
        val newDevice = createTestsSetupDeviceDto(
            entities = listOf(
                createTestSetupEntityDto(key = "1"),
                createTestSetupEntityDto(key = "1")
            )
        )

        // when

        // then
        assertThrows<ExistingEntityKeyException> { deviceService.setupDevice(newDevice) }
    }

    @Test
    fun `given existing device, when setting up same device and different entities, then will update entities`() {
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
        existingDevice.entities = mutableListOf(
            createTestEntity(
                key = "temp",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "F"
            ),
            createTestEntity(
                key = "hum",
                type = EntityType.SENSOR,
                unitType = UnitType.INTEGER,
                unitDisplayName = "%"
            ),
            createTestEntity(
                key = "online",
                type = EntityType.SENSOR,
                unitType = UnitType.BOOLEAN,
                unitDisplayName = ""
            )
        )

        // when
        val newDevice = existingDevice.toSetupDeviceDto()

        deviceService.setupDevice(newDevice)

        // then
        val updatedDevice = deviceRepository.findByKey(newDevice.key).get()
        assertThat(updatedDevice.entities).hasSize(3)
        assertThat(updatedDevice.entities.any { it.unitDisplayName == "F" })
    }

    @Test
    fun `given existing device, when setting up different device and same entities, then will update device`() {
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
        val newDevice = existingDevice.toSetupDeviceDto()

        deviceService.setupDevice(newDevice)

        // then
        val updatedDevice = deviceRepository.findByKey(newDevice.key).get()
        assertThat(updatedDevice.entities).hasSize(2)
        assertThat(updatedDevice.displayName).isEqualTo("device1")
    }

    @Test
    fun `given existing device, when setting up same device with entity deleted, then will delete entity`() {
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
        existingDevice.entities = mutableListOf(
            createTestEntity(
                key = "temp",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
            ),
        )

        // when
        val newDevice = existingDevice.toSetupDeviceDto()

        deviceService.setupDevice(newDevice)

        // then
        val updatedDevice = deviceRepository.findByKey(newDevice.key).get()
        assertThat(updatedDevice.entities).hasSize(1)
    }

    @Test
    fun `given existing device, when setting up new device with same macAddress and different key, then will throw ExistingMACAddressException`() {
        // given
        val existingDevice = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestsSetupDeviceDto(id = "device2", mac = "00:A0:C9:14:C8:29")

        // then
        assertThrows<ExistingMACAddressException> { deviceService.setupDevice(newDevice) }
    }

    @Test
    fun `given new device, when getting all devices, then will be one device`() {
        // given
        val newDevice = createTestDevice()
        deviceRepository.save(newDevice)

        // when
        val devices = deviceService.getAllDevices()

        // then
        assertThat(devices).hasSize(1)
    }

    @Test
    fun `given new device, when getting device by id, then will return one`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val device = deviceService.getDeviceById(savedDevice.id)

        // then
        assertThat(device).isNotNull
    }

    @Test
    fun `given no device, when getting device by id, then will throw DeviceNotFoundException`() {
        // given

        // when
        assertThrows<DeviceNotFoundException> { deviceService.getDeviceById(999) }

        // then
    }

    @Test
    fun `given existing device when deleting device by id, then will delete one`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        deviceService.deleteDeviceById(savedDevice.id)

        // then
        assertThat(deviceRepository.findAll()).isEmpty()
        assertThat(entityRepository.findByDeviceId(savedDevice.id)).isEmpty()
    }

    @Test
    fun `given existing device when updating device by id, then will update one`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val updateDeviceDto = UpdateDeviceDto(
            displayName = testNewDisplayName,
            icon = MaterialIcon.SENSORS
        )
        deviceService.updateDeviceById(savedDevice.id, updateDeviceDto)

        // then
        val updatedDeviceOptional = deviceRepository.findById(savedDevice.id)
        assertThat(updatedDeviceOptional.isPresent).isTrue
        val updatedDevice = updatedDeviceOptional.get()
        assertThat(updatedDevice.displayName).isEqualTo(updateDeviceDto.displayName)
        assertThat(updatedDevice.icon).isEqualTo(updateDeviceDto.icon)
    }

    @Test
    fun `given no device when updating device by id, then will throw DeviceNotFoundException`() {
        // given

        // when
        val updateDeviceDto = UpdateDeviceDto(
            displayName = testNewDisplayName,
            icon = MaterialIcon.SENSORS
        )
        assertThrows<DeviceNotFoundException> { deviceService.updateDeviceById(99999, updateDeviceDto) }

        // then
    }

    @Test
    fun `given no device when patching device by id, then will throw DeviceNotFoundException`() {
        // given

        // when
        val devicePatch = mapOf(
            displayNameKey to testNewDisplayName
        )
        assertThrows<DeviceNotFoundException> { deviceService.patchDeviceById(99999, devicePatch) }

        // then
    }

    @Test
    fun `given existing device when patching device with one field by id, then will patch one`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val devicePatch = mapOf(
            displayNameKey to testNewDisplayName
        )
        deviceService.patchDeviceById(savedDevice.id, devicePatch)

        // then
        val updatedDeviceOptional = deviceRepository.findById(savedDevice.id)
        assertThat(updatedDeviceOptional.isPresent).isTrue
        val updatedDevice = updatedDeviceOptional.get()
        assertThat(updatedDevice.displayName).isEqualTo(devicePatch[displayNameKey])
        assertThat(updatedDevice.icon).isEqualTo(savedDevice.icon)
    }

    @Test
    fun `given existing device when patching device with enum field with invalid value, then will throw InvalidDevicePatchEntryValueException`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val devicePatch = mapOf(
            "icon" to "SENSORSS"
        )
        assertThrows<InvalidDevicePatchEntryValueException> { deviceService.patchDeviceById(savedDevice.id, devicePatch) }

        // then
    }

    @Test
    fun `given existing device when patching device with two fields by id, then will patch two`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val devicePatch = mapOf(
            displayNameKey to testNewDisplayName,
            "icon" to "SENSORS"
        )
        deviceService.patchDeviceById(savedDevice.id, devicePatch)

        // then
        val updatedDeviceOptional = deviceRepository.findById(savedDevice.id)
        assertThat(updatedDeviceOptional.isPresent).isTrue
        val updatedDevice = updatedDeviceOptional.get()
        assertThat(updatedDevice.displayName).isEqualTo(devicePatch[displayNameKey])
        assertThat(updatedDevice.icon).isEqualTo(MaterialIcon.SENSORS)
    }

    @Test
    fun `given existing device when patching device with one invalid type field by id, then will throw InvalidDevicePatchEntryValueException`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val devicePatch = mapOf(
            displayNameKey to 123,
        )
        assertThrows<InvalidDevicePatchEntryValueException> { deviceService.patchDeviceById(savedDevice.id, devicePatch) }

        // then
    }

    @Test
    fun `given existing device when patching device with no by id, then will not patch`() {
        // given
        val newDevice = createTestDevice()
        val savedDevice = deviceRepository.save(newDevice)

        // when
        val devicePatch = emptyMap<String, Any>()
        deviceService.patchDeviceById(savedDevice.id, devicePatch)

        // then
        val updatedDeviceOptional = deviceRepository.findById(savedDevice.id)
        assertThat(updatedDeviceOptional.isPresent).isTrue
        val updatedDevice = updatedDeviceOptional.get()
        assertThat(updatedDevice.displayName).isEqualTo(savedDevice.displayName)
        assertThat(updatedDevice.icon).isEqualTo(savedDevice.icon)
    }
}
