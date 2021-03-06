/* ktlint-disable max-line-length */
package io.kni.thingoo.backend.integration.readings

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.exceptions.DeviceNotFoundException
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.ValueType
import io.kni.thingoo.backend.entities.exceptions.EntityNotFoundException
import io.kni.thingoo.backend.readings.Reading
import io.kni.thingoo.backend.readings.ReadingRepository
import io.kni.thingoo.backend.readings.ReadingService
import io.kni.thingoo.backend.readings.dto.SaveReadingDto
import io.kni.thingoo.backend.readings.exceptions.NoReadingsException
import io.kni.thingoo.backend.readings.exceptions.ReadingValueTypeMismatchException
import io.kni.thingoo.backend.utils.createTestDevice
import io.kni.thingoo.backend.utils.createTestEntity
import io.kni.thingoo.backend.utils.saveDevice
import io.kni.thingoo.backend.utils.saveEntities
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class ReadingServiceTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Autowired
    private lateinit var readingRepository: ReadingRepository

    @Autowired
    private lateinit var readingService: ReadingService

    companion object {
        private val TEST_DEVICE_1 = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29", name = "device1")
        private val TEST_DEVICE_2 = createTestDevice(key = "device2", mac = "00:A0:C9:14:C8:28", name = "device2")
        private val TEST_ENTITY_1 = createTestEntity(
            key = "temp",
            name = "temperature",
            type = EntityType.SENSOR,
            valueType = ValueType.DECIMAL,
            unitDisplayName = "C"
        )
        private val TEST_ENTITY_2 = createTestEntity(
            key = "hum",
            name = "humidity",
            type = EntityType.SENSOR,
            valueType = ValueType.INTEGER,
            unitDisplayName = "%"
        )
        private val TEST_ENTITY_3 = createTestEntity(
            key = "status",
            name = "status",
            type = EntityType.SENSOR,
            valueType = ValueType.STRING,
            unitDisplayName = ""
        )
        private val TEST_ENTITY_4 = createTestEntity(
            key = "on",
            name = "on/off",
            type = EntityType.SENSOR,
            valueType = ValueType.BOOLEAN,
            unitDisplayName = ""
        )
    }

    @AfterEach
    fun clear() {
        readingRepository.deleteAll()
        entityRepository.deleteAll()
        deviceRepository.deleteAll()
    }

    @Test
    fun `given device and entity, when saving new reading, will save one reading`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        // when
        val newReading = readingService.createReading(SaveReadingDto(value = "10.5", entityKey = TEST_ENTITY_1.key, deviceKey = TEST_DEVICE_1.key))

        // then
        val readingOptional = readingRepository.findById(newReading.id)
        assertThat(readingOptional.isPresent).isTrue
        val reading = readingOptional.get()
        assertThat(reading).isNotNull
        assertThat(reading.id).isNotEqualTo(0)
        assertThat(reading.value).isEqualTo("10.5")
    }

    @Test
    fun `given device and entity, when saving reading with not existing deviceKey, will throw DeviceNotFoundException`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        // when
        assertThrows<DeviceNotFoundException> {
            readingService.createReading(SaveReadingDto(value = "someValue", entityKey = TEST_ENTITY_1.key, deviceKey = "33211"))
        }
    }

    @Test
    fun `given device and entity, when saving reading with not existing entityKey, will throw EntityNotFoundException`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        // when
        assertThrows<EntityNotFoundException> {
            readingService.createReading(SaveReadingDto(value = "someValue", entityKey = "33211", deviceKey = TEST_DEVICE_1.key))
        }
    }

    @Test
    fun `given device and entity, when saving reading with wrong value type, will throw ReadingValueTypeMismatchException`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2, TEST_ENTITY_3, TEST_ENTITY_4))

        // when
        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "true", entityKey = TEST_ENTITY_1.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "wrong value", entityKey = TEST_ENTITY_1.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "55", entityKey = TEST_ENTITY_1.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "false", entityKey = TEST_ENTITY_2.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "wrong value", entityKey = TEST_ENTITY_2.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "55.49", entityKey = TEST_ENTITY_2.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "55.49", entityKey = TEST_ENTITY_4.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "55", entityKey = TEST_ENTITY_4.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertThrows<ReadingValueTypeMismatchException> {
            readingService.createReading(SaveReadingDto(value = "wrong value", entityKey = TEST_ENTITY_4.key, deviceKey = TEST_DEVICE_1.key))
        }

        assertDoesNotThrow {
            readingService.createReading(SaveReadingDto(value = "proper value", entityKey = TEST_ENTITY_3.key, deviceKey = TEST_DEVICE_1.key))
        }
    }

    @Test
    fun `given multiple devices, when querying readings by entityID, will return appropriate readings`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        val device2 = saveDevice(deviceRepository, TEST_DEVICE_2)
        val entities2 = saveEntities(entityRepository, device2, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        readingRepository.save(Reading(value = "55.49", entity = entities[0]))
        readingRepository.save(Reading(value = "55.51", entity = entities[0]))
        readingRepository.save(Reading(value = "11", entity = entities2[1]))

        // when
        val readings = readingService.getReadingsByEntityId(entities[0].id)
        val readings1 = readingService.getReadingsByEntityId(entities[1].id)
        val readings2 = readingService.getReadingsByEntityId(entities2[0].id)
        val readings3 = readingService.getReadingsByEntityId(entities2[1].id)

        // then
        assertThat(readings).hasSize(2)
        assertThat(readings1).hasSize(0)
        assertThat(readings2).hasSize(0)
        assertThat(readings3).hasSize(1)
    }

    @Test
    fun `given multiple devices, when querying readings by deviceKey and entityKey, will return appropriate readings`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        val device2 = saveDevice(deviceRepository, TEST_DEVICE_2)
        val entities2 = saveEntities(entityRepository, device2, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        readingRepository.save(Reading(value = "55.49", entity = entities[0]))
        readingRepository.save(Reading(value = "55.51", entity = entities[0]))
        readingRepository.save(Reading(value = "11", entity = entities2[1]))

        // when
        val readings = readingService.getReadingsByDeviceKeyAndEntityKey(TEST_DEVICE_1.key, TEST_ENTITY_1.key)
        val readings1 = readingService.getReadingsByDeviceKeyAndEntityKey(TEST_DEVICE_1.key, TEST_ENTITY_2.key)
        val readings2 = readingService.getReadingsByDeviceKeyAndEntityKey(TEST_DEVICE_2.key, TEST_ENTITY_1.key)
        val readings3 = readingService.getReadingsByDeviceKeyAndEntityKey(TEST_DEVICE_2.key, TEST_ENTITY_2.key)

        // then
        assertThat(readings).hasSize(2)
        assertThat(readings1).hasSize(0)
        assertThat(readings2).hasSize(0)
        assertThat(readings3).hasSize(1)
    }

    @Test
    fun `given device with entities, when querying latest reading by deviceKey and entityKey, will return appropriate reading`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        readingRepository.save(Reading(value = "55.49", entity = entities[0]))
        readingRepository.save(Reading(value = "55.50", entity = entities[0]))
        readingRepository.save(Reading(value = "55.51", entity = entities[0]))

        // when
        val reading = readingService.getLatestReadingByDeviceKeyAndEntityKey(TEST_DEVICE_1.key, TEST_ENTITY_1.key)

        // then
        assertThat(reading.value).isEqualTo("55.51")
    }

    @Test
    fun `given device with entities, when querying latest reading by entityId, will return appropriate reading`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        readingRepository.save(Reading(value = "55.49", entity = entities[0]))
        readingRepository.save(Reading(value = "55.50", entity = entities[0]))
        readingRepository.save(Reading(value = "55.51", entity = entities[0]))

        // when
        val reading = readingService.getLatestReadingByEntityId(entities[0].id)

        // then
        assertThat(reading.value).isEqualTo("55.51")
    }

    @Test
    fun `given entity with no readings, when querying latest reading by entityId, will throw NoReadingsException`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        // when
        assertThrows<NoReadingsException> {
            readingService.getLatestReadingByEntityId(entities[0].id)
        }

        // then
    }

    @Test
    fun `given entity with no readings, when querying latest reading by deviceKey and entityKey, will throw NoReadingsException`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        saveEntities(entityRepository, device, listOf(TEST_ENTITY_1, TEST_ENTITY_2))

        // when
        assertThrows<NoReadingsException> {
            readingService.getLatestReadingByDeviceKeyAndEntityKey(TEST_DEVICE_1.key, TEST_ENTITY_1.key)
        }

        // then
    }
}
