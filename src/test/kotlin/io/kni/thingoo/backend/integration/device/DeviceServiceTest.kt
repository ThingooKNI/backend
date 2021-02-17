package io.kni.thingoo.backend.integration.device

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.entities.EntityRepository
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
    fun `given new device, when registering new device, will create new device with entities`() {
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
}
