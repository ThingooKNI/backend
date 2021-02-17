package io.kni.thingoo.backend.integration.device

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.devices.ExistingDeviceIDException
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.ExistingEntityKeyException
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureEmbeddedDatabase
class DeviceServiceTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Autowired
    private lateinit var deviceService: DeviceService

    @Test
    fun `given new device, when registering new device, then will register new device with entities`() {
        // given
        val newDevice = createTestRegisterDeviceDto()
        newDevice.entities = setOf(
            createTestRegisterEntityDto(key = "1"),
            createTestRegisterEntityDto(key = "2")
        )

        // when
        deviceService.registerDevice(newDevice)

        // then
        val createdDeviceOptional = deviceRepository.findByDeviceID(newDevice.deviceID)
        assertThat(createdDeviceOptional.isPresent).isTrue
        val createdDevice = createdDeviceOptional.get()
        assertThat(createdDevice.id).isNotEqualTo(0)
        assertThat(createdDevice.entities).hasSize(2)
    }

    @Test
    fun `given existing device, when registering new device with same deviceID and same mac address, then will register one`() {
        // given
        val existingDevice = createTestDevice(id = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestRegisterDeviceDto(id = "device1", mac = "00:A0:C9:14:C8:29")
        deviceService.registerDevice(newDevice)

        // then
        val createdDeviceOptional = deviceRepository.findByDeviceID(newDevice.deviceID)
        assertThat(createdDeviceOptional.isPresent).isTrue
    }

    @Test
    fun `given existing device, when registering new device with same deviceID and different mac address, then will throw ExistingDeviceIDException`() {
        // given
        val existingDevice = createTestDevice(id = "device1", mac = "00:A0:C9:14:C8:29")
        deviceRepository.save(existingDevice)

        // when
        val newDevice = createTestRegisterDeviceDto(id = "device1", mac = "00:A5:D3:26:C2:37")


        // then
        assertThrows<ExistingDeviceIDException> { deviceService.registerDevice(newDevice) }
    }

    @Test
    fun `given new device, when registering new device with duplicated entities, then will throw`() {
        // given
        val newDevice = createTestRegisterDeviceDto()
        newDevice.entities = setOf(
            createTestRegisterEntityDto(key = "1"),
            createTestRegisterEntityDto(key = "1")
        )

        // when


        // then
        assertThrows<ExistingEntityKeyException> { deviceService.registerDevice(newDevice) }
    }

    @Test
    fun `given existing device, when registering same device and different entities, then will update entities`() {
        TODO()
    }

    @Test
    fun `given existing device, when registering different device and same entities, then will update device`() {
        TODO()
    }
}
