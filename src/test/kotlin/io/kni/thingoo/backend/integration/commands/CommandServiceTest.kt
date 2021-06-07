package io.kni.thingoo.backend.integration.commands

import io.kni.thingoo.backend.commands.CommandServiceImpl
import io.kni.thingoo.backend.commands.dto.NewCommandDto
import io.kni.thingoo.backend.commands.exceptions.CommandValueTypeMismatchException
import io.kni.thingoo.backend.commands.exceptions.NonActuatorCommandException
import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.entities.ValueType
import io.kni.thingoo.backend.entities.exceptions.EntityNotFoundException
import io.kni.thingoo.backend.mqtt.MqttService
import io.kni.thingoo.backend.utils.createTestDevice
import io.kni.thingoo.backend.utils.createTestEntity
import io.kni.thingoo.backend.utils.saveDevice
import io.kni.thingoo.backend.utils.saveEntities
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandServiceTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    private lateinit var mqttService: MqttService

    private lateinit var commandService: CommandServiceImpl

    @BeforeAll
    fun setup() {
        mqttService = Mockito.mock(MqttService::class.java)
        commandService = CommandServiceImpl(entityRepository, mqttService)
    }

    @AfterEach
    fun clear() {
        entityRepository.deleteAll()
        deviceRepository.deleteAll()
    }

    companion object {
        private val TEST_DEVICE_1 = createTestDevice(key = "device1", mac = "00:A0:C9:14:C8:29", name = "device1")
        private val TEST_ENTITY_1 = createTestEntity(
            key = "temp",
            name = "temperature",
            type = EntityType.SENSOR,
            valueType = ValueType.DECIMAL,
            unitDisplayName = "C"
        )
        private val TEST_ENTITY_2 = createTestEntity(
            key = "light",
            name = "light switch",
            type = EntityType.ACTUATOR,
            valueType = ValueType.STRING,
            unitDisplayName = "C"
        )
        private val TEST_ENTITY_3 = createTestEntity(
            key = "thermostat",
            name = "thermostat",
            type = EntityType.ACTUATOR,
            valueType = ValueType.INTEGER,
            unitDisplayName = "C"
        )
    }

    @Test
    fun `given no entity, when sending new command, will throw EntityNotFoundException`() {
        // given

        // when
        assertThrows<EntityNotFoundException> { commandService.sendNewCommandToEntity(NewCommandDto("test"), 999999) }
    }

    @Test
    fun `given non-actuator entity, when sending new command, will throw NonActuatorCommandException`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_1))

        // when
        assertThrows<NonActuatorCommandException> { commandService.sendNewCommandToEntity(NewCommandDto("test"), entities[0].id) }
    }

    @Test
    fun `given actuator entity, when sending new command, will send one`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_2))

        // when
        commandService.sendNewCommandToEntity(NewCommandDto("test"), entities[0].id)

        // then
        Mockito.verify(mqttService).publish("test", "/devices/device1/entities/light/command", 1, false)
    }

    @Test
    fun `given actuator entity, when sending new empty command, will send one`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_2))

        // when
        commandService.sendNewCommandToEntity(NewCommandDto(value = null), entities[0].id)

        // then
        Mockito.verify(mqttService).publish("", "/devices/device1/entities/light/command", 1, false)
    }

    @Test
    fun `given actuator entity, when sending empty command with incorrect value type, will throw`() {
        // given
        val device = saveDevice(deviceRepository, TEST_DEVICE_1)
        val entities = saveEntities(entityRepository, device, listOf(TEST_ENTITY_3))

        // when
        assertThrows<CommandValueTypeMismatchException> { commandService.sendNewCommandToEntity(NewCommandDto(value = "wrong value"), entities[0].id) }

        // then
    }
}
