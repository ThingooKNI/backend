package io.kni.thingoo.backend.integration.devices

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.UnitType
import io.kni.thingoo.backend.icons.MaterialIcon
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class DeviceRepositoryTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @AfterEach
    fun clear() {
        deviceRepository.deleteAll()
        entityRepository.deleteAll()
    }

    @Test
    fun saveDeviceWithEntity() {
        val device = Device(key = "test", macAddress = "00:0a:95:9d:68:16", displayName = "Test device", icon = null)

        deviceRepository.save(device)
        assertThat(device.id).isNotEqualTo(0)

        val entities = listOf(
            Entity(
                key = "temp",
                displayName = "Temperature",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
                device = device,
                icon = null
            )
        )

        entityRepository.saveAll(entities)

        val savedDevice = deviceRepository.findById(device.id)
        assertThat(savedDevice.isPresent).isTrue
        assertThat(savedDevice.get().id).isNotEqualTo(0)
        assertThat(savedDevice.get().entities).hasSize(1)
    }

    @Test
    fun saveDeviceWithEntityWithIcons() {
        val device = Device(key = "test", macAddress = "00:0a:95:9d:68:16", displayName = "Test device", icon = MaterialIcon.SENSORS)

        deviceRepository.save(device)
        assertThat(device.id).isNotEqualTo(0)

        val entities = listOf(
            Entity(
                key = "temp",
                displayName = "Temperature",
                type = EntityType.SENSOR,
                unitType = UnitType.DECIMAL,
                unitDisplayName = "C",
                device = device,
                icon = MaterialIcon.THERMOSTAT
            )
        )

        entityRepository.saveAll(entities)

        val savedDevice = deviceRepository.findById(device.id)
        assertThat(savedDevice.isPresent).isTrue
        assertThat(savedDevice.get().id).isNotEqualTo(0)
        assertThat(savedDevice.get().entities).hasSize(1)
        assertThat(savedDevice.get().icon).isEqualTo(MaterialIcon.SENSORS)
        assertThat(savedDevice.get().entities[0].icon).isEqualTo(MaterialIcon.THERMOSTAT)
    }
}
