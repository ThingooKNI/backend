package io.kni.thingoo.backend.integration.device

import io.kni.thingoo.backend.devices.DeviceRepository
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

    @Test
    fun `given no device, when registering new device, will create new device with entities`() {
        // given

        // when
        val newDevice = createTestDevice()
        val id = deviceRepository.save(newDevice).id

        val createdDevice = deviceRepository.findById(id).get()
        assertThat(createdDevice.id).isNotEqualTo(0)
    }
}
