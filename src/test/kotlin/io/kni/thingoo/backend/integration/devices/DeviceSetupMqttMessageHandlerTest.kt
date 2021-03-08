package io.kni.thingoo.backend.integration.devices

import io.kni.thingoo.backend.devices.DeviceRepository
import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.devices.DeviceSetupMqttMessageHandler
import io.kni.thingoo.backend.devices.exceptions.InvalidDeviceSetupJsonException
import io.kni.thingoo.backend.entities.EntityRepository
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
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

    @Test
    fun `given wrong json1, when handling setup message, will throw InvalidDeviceSetupJsonException`() {
        //given

        // when
        assertThrows<InvalidDeviceSetupJsonException> {
            deviceSetupMqttMessageHandler.handle("wrong json", "/devices/newDevice/setup")
        }

        // then
    }

    @Test
    fun `given wrong json2, when handling setup message, will throw InvalidDeviceSetupJsonException`() {
        //given

        // when
        assertThrows<InvalidDeviceSetupJsonException> {
            deviceSetupMqttMessageHandler.handle("{\n" +
                "  \"macAddress\": \"c0:3e:ba:c3:50:0b\",\n" +
                "  \"entities\": [\n" +
                "    {\n" +
                "      \"key\": \"temp\",\n" +
                "      \"type\": \"SENSOR\",\n" +
                "      \"unitType\": \"DECIMAL\",\n" +
                "      \"unitDisplayName\": \"C\"\n" +
                "    }\n" +
                "  ]\n" +
                "}", "/devices/newDevice/setup")
        }

        // then
    }
}
