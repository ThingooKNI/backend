package io.kni.thingoo.backend.integration.entities

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityService
import io.kni.thingoo.backend.entities.dto.UpdateEntityDto
import io.kni.thingoo.backend.entities.exceptions.EntityNotFoundException
import io.kni.thingoo.backend.entities.exceptions.InvalidEntityPatchEntryValueException
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
    private lateinit var entityService: EntityService

    @AfterEach
    fun clear() {
        deviceRepository.deleteAll()
        entityRepository.deleteAll()
    }

    val testDisplayName = "test entity"
    val testNewDisplayName = "newName"
    val displayNameKey = "displayName"
    val iconKey = "icon"

    @Test
    fun `given existing entity when updating entity by id, then will update one`() {
        // given
        val existingDevice = createTestDevice()
        deviceRepository.save(existingDevice)

        val existingEntities = listOf(createTestEntity(device = existingDevice))
        val savedEntities = entityRepository.saveAll(existingEntities).toList()

        // when
        val updateEntityDto = UpdateEntityDto(
            displayName = testDisplayName,
            icon = MaterialIcon.THERMOSTAT
        )
        entityService.updateEntity(savedEntities[0].id, updateEntityDto)

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
            displayName = testDisplayName,
            icon = MaterialIcon.THERMOSTAT
        )

        assertThrows<EntityNotFoundException> { entityService.updateEntity(99999, updateEntityDto) }

        // then
    }

    @Test
    fun `given no entity when patching entity by id, then will throw EntityNotFoundException`() {
        // given

        // when
        val entityPatch = mapOf(
            displayNameKey to testNewDisplayName
        )
        assertThrows<EntityNotFoundException> { entityService.patchEntity(99999, entityPatch) }

        // then
    }

    @Test
    fun `given existing device when patching device with one field by id, then will patch one`() {
        // given
        val existingDevice = createTestDevice()
        deviceRepository.save(existingDevice)

        val existingEntities = listOf(createTestEntity(device = existingDevice))
        val savedEntities = entityRepository.saveAll(existingEntities).toList()

        // when
        val entityPatch = mapOf(
            displayNameKey to testNewDisplayName
        )
        entityService.patchEntity(savedEntities[0].id, entityPatch)

        // then
        val updatedEntityOptional = entityRepository.findById(savedEntities[0].id)
        assertThat(updatedEntityOptional.isPresent).isTrue
        val updatedEntity = updatedEntityOptional.get()
        assertThat(updatedEntity.displayName).isEqualTo(entityPatch[displayNameKey])
        assertThat(updatedEntity.icon).isEqualTo(savedEntities[0].icon)
    }

    @Test
    fun `given existing device when patching device with enum field with invalid value, then will throw InvalidDevicePatchEntryValueException`() {
        // given
        val existingDevice = createTestDevice()
        deviceRepository.save(existingDevice)

        val existingEntities = listOf(createTestEntity(device = existingDevice))
        val savedEntities = entityRepository.saveAll(existingEntities).toList()

        // when
        val entityPatch = mapOf(
            iconKey to "SENSORSS"
        )
        assertThrows<InvalidEntityPatchEntryValueException> { entityService.patchEntity(savedEntities[0].id, entityPatch) }

        // then
    }

    @Test
    fun `given existing device when patching device with two fields by id, then will patch two`() {
        // given
        val existingDevice = createTestDevice()
        deviceRepository.save(existingDevice)

        val existingEntities = listOf(createTestEntity(device = existingDevice))
        val savedEntities = entityRepository.saveAll(existingEntities).toList()

        // when
        val entityPatch = mapOf(
            displayNameKey to testNewDisplayName,
            iconKey to "SENSORS"
        )
        entityService.patchEntity(savedEntities[0].id, entityPatch)

        // then
        val updatedEntityOptional = entityRepository.findById(savedEntities[0].id)
        assertThat(updatedEntityOptional.isPresent).isTrue
        val updatedEntity = updatedEntityOptional.get()
        assertThat(updatedEntity.displayName).isEqualTo(entityPatch[displayNameKey])
        assertThat(updatedEntity.icon).isEqualTo(MaterialIcon.SENSORS)
    }

    @Test
    fun `given existing device when patching device with one invalid type field by id, then will throw InvalidDevicePatchEntryValueException`() {
        // given
        val existingDevice = createTestDevice()
        deviceRepository.save(existingDevice)

        val existingEntities = listOf(createTestEntity(device = existingDevice))
        val savedEntities = entityRepository.saveAll(existingEntities).toList()

        // when
        val entityPatch = mapOf(
            displayNameKey to 123
        )
        assertThrows<InvalidEntityPatchEntryValueException> { entityService.patchEntity(savedEntities[0].id, entityPatch) }

        // then
    }

    @Test
    fun `given existing device when patching device with no by id, then will not patch`() {
        // given
        val existingDevice = createTestDevice()
        deviceRepository.save(existingDevice)

        val existingEntities = listOf(createTestEntity(device = existingDevice))
        val savedEntities = entityRepository.saveAll(existingEntities).toList()

        // when
        val entityPatch = emptyMap<String, Any>()
        entityService.patchEntity(savedEntities[0].id, entityPatch)

        // then
        val updatedEntityOptional = entityRepository.findById(savedEntities[0].id)
        assertThat(updatedEntityOptional.isPresent).isTrue
        val updatedEntity = updatedEntityOptional.get()
        assertThat(updatedEntity.displayName).isEqualTo(savedEntities[0].displayName)
        assertThat(updatedEntity.icon).isEqualTo(savedEntities[0].icon)
    }
}
