package io.kni.thingoo.backend.integration.entities

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityService
import io.kni.thingoo.backend.entities.dto.UpdateEntityDto
import io.kni.thingoo.backend.entities.exceptions.EntityNotFoundException
import io.kni.thingoo.backend.icons.MaterialIcon
import io.kni.thingoo.backend.integration.devices.createTestDevice
import io.kni.thingoo.backend.integration.devices.createTestEntity
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
class EntityServiceTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var entityService: EntityService

    @AfterEach
    fun clear() {
        deviceRepository.deleteAll()
        entityRepository.deleteAll()
    }

    @Test
    fun `given existing entity when updating entity by id, then will update one`() {
        // given
        val existingDevice = createTestDevice()
        deviceRepository.save(existingDevice)

        val existingEntities = listOf(createTestEntity(device = existingDevice))
        val savedEntities = entityRepository.saveAll(existingEntities).toList()

        // when
        val updateEntityDto = UpdateEntityDto(
            displayName = "test entity",
            icon = MaterialIcon.THERMOSTAT
        )
        entityService.updateEntity(updateEntityDto, savedEntities[0].id)

        // then
        val updatedEntityOptional = entityRepository.findById(savedEntities[0].id)
        assertThat(updatedEntityOptional.isPresent).isTrue
        val updatedEntity = updatedEntityOptional.get()
        assertThat(updatedEntity.displayName).isEqualTo(updateEntityDto.displayName)
        assertThat(updatedEntity.icon).isEqualTo(updateEntityDto.icon)
    }

    @Test
    fun `given no entity when updating entity by id, then will throw EntityNotFoundException`() {
        // given

        // when
        val updateEntityDto = UpdateEntityDto(
            displayName = "test entity",
            icon = MaterialIcon.THERMOSTAT
        )

        assertThrows<EntityNotFoundException> { entityService.updateEntity(updateEntityDto, 99999) }

        // then
    }
}
