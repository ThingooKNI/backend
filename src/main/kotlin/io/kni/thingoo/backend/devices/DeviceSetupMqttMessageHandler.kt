package io.kni.thingoo.backend.devices

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto
import io.kni.thingoo.backend.devices.exceptions.InvalidDeviceSetupJsonException
import io.kni.thingoo.backend.mqtt.MqttMessageHandler
import org.springframework.stereotype.Component

@Component
class DeviceSetupMqttMessageHandler(
    private val deviceService: DeviceService,
    private val objectMapper: ObjectMapper
) : MqttMessageHandler {

    companion object {
        val deviceSetupTopicRegex = Regex("^/devices/(\\w+)/setup$")
    }

    override fun handle(message: String, topic: String) {
        val (deviceKey) = getKeysFromTopic(topic)

        try {
            val setupDeviceDto = objectMapper.readValue(message, SetupDeviceDto::class.java)

            if (deviceKey != setupDeviceDto.key) {
                throw InvalidDeviceSetupJsonException("Provided device key is different from device key in the MQTT topic")
            }

            deviceService.setupDevice(setupDeviceDto)
        } catch (e: JsonMappingException) {
            throw InvalidDeviceSetupJsonException("Provided setup config is invalid: ${e.message}")
        } catch (e: JsonParseException) {
            throw InvalidDeviceSetupJsonException("Provided setup config is invalid: ${e.message}")
        }
    }

    private fun getKeysFromTopic(topic: String): MatchResult.Destructured {
        return deviceSetupTopicRegex.find(topic)!!.destructured
    }
}
