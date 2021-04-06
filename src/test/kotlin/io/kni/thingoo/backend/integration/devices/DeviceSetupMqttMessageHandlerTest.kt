package io.kni.thingoo.backend.integration.devices

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.devices.DeviceSetupMqttMessageHandler
import io.kni.thingoo.backend.devices.exceptions.InvalidDeviceSetupJsonException
import io.kni.thingoo.backend.entities.EntityRepository
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
class DeviceSetupMqttMessageHandlerTest {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var deviceSetupMqttMessageHandler: DeviceSetupMqttMessageHandler

    @AfterEach
    fun clear() {
        deviceRepository.deleteAll()
        entityRepository.deleteAll()
    }

    private val defaultDeviceKey = "newDevice"
    private val defaultTopic = "/devices/$defaultDeviceKey/setup"

    @Test
    fun `given wrong json1, when handling setup message, will throw InvalidDeviceSetupJsonException`() {
        // given

        // when
        assertThrows<InvalidDeviceSetupJsonException> {
            deviceSetupMqttMessageHandler.handle("wrong json", defaultTopic)
        }

        // then
    }

    @Test
    fun `given wrong json2, when handling setup message, will throw InvalidDeviceSetupJsonException`() {
        // given
        val setupJson = "{\n" +
            "  \"macAddress\": \"c0:3e:ba:c3:50:0b\",\n" +
            "  \"entities\": [\n" +
            "    {\n" +
            "      \"key\": \"temp\",\n" +
            "      \"type\": \"SENSOR\",\n" +
            "      \"valueType\": \"DECIMAL\",\n" +
            "      \"unitDisplayName\": \"C\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"

        // when
        assertThrows<InvalidDeviceSetupJsonException> {
            deviceSetupMqttMessageHandler.handle(
                setupJson,
                defaultTopic
            )
        }

        // then
    }

    @Test
    fun `given wrong device key in topic, when handling setup message, will throw InvalidDeviceSetupJsonException`() {
        // given
        val setupJson = "{\n" +
            "  \"key\": \"test\",\n" +
            "  \"macAddress\": \"c0:3e:ba:c3:50:0b\",\n" +
            "  \"entities\": [\n" +
            "    {\n" +
            "      \"key\": \"temp\",\n" +
            "      \"type\": \"SENSOR\",\n" +
            "      \"valueType\": \"DECIMAL\",\n" +
            "      \"unitDisplayName\": \"C\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"

        // when
        assertThrows<InvalidDeviceSetupJsonException> {
            deviceSetupMqttMessageHandler.handle(
                setupJson,
                defaultTopic
            )
        }

        // then
    }

    @Test
    fun `given proper json, when handling setup message, will setup new device`() {
        // given
        val setupJson = "{\n" +
            "  \"key\": \"${defaultDeviceKey}\",\n" +
            "  \"macAddress\": \"c0:3e:ba:c3:50:0b\",\n" +
            "  \"entities\": [\n" +
            "    {\n" +
            "      \"key\": \"temp\",\n" +
            "      \"type\": \"SENSOR\",\n" +
            "      \"valueType\": \"DECIMAL\",\n" +
            "      \"unitDisplayName\": \"C\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"

        // when
        deviceSetupMqttMessageHandler.handle(
            setupJson,
            defaultTopic
        )

        // then
        val device = deviceRepository.findByKey(defaultDeviceKey)
        assertThat(device.isPresent).isTrue
    }
}
