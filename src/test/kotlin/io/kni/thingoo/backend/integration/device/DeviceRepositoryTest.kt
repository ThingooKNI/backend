package io.kni.thingoo.backend.integration.device

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@AutoConfigureEmbeddedDatabase
@ActiveProfiles("test")
class DeviceRepositoryTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun saveDeviceWithEntity() {
        val device = Device(deviceID = "test", macAddress = "00:0a:95:9d:68:16", displayName = "Test device")

        deviceRepository.save(device)
        assertThat(device.id).isNotEqualTo(0)

        val entities = setOf(
            Entity(
                key = "temp",
                displayName = "Temperature",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
                device = device
            )
        )

        entityRepository.saveAll(entities)

        val savedDevice = deviceRepository.findById(device.id)
        assertThat(savedDevice.isPresent).isTrue
        assertThat(savedDevice.get().id).isEqualTo(1)
        assertThat(savedDevice.get().entities).hasSize(1)
        assertThat(savedDevice.get().entities.first().id).isEqualTo(1)
    }
}
