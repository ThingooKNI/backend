package io.kni.thingoo.backend.devices

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.kni.thingoo.backend.devices.dto.RegisterDeviceDto
import io.kni.thingoo.backend.devices.exceptions.InvalidDeviceSetupJsonException
import io.kni.thingoo.backend.mqtt.MqttMessage
import io.kni.thingoo.backend.mqtt.MqttMessageHandler
import org.springframework.stereotype.Service

@Service
class DeviceSetupMqttMessageHandler(
    private val deviceService: DeviceService,
    private val objectMapper: ObjectMapper
) : MqttMessageHandler {

    companion object {
        val deviceSetupTopicRegex = Regex("^/devices/(\\w+)/setup$")
    }

    override fun handle(message: String, topic: String): MqttMessage? {
        val (deviceKey) = getDestructuredTopic(topic)

        try {
            val registerDeviceDto = objectMapper.readValue(message, RegisterDeviceDto::class.java)

            if (deviceKey != registerDeviceDto.key) {
                throw InvalidDeviceSetupJsonException("Provided device key is different from device key in the MQTT topic")
            }

            deviceService.registerDevice(registerDeviceDto)

            return MqttMessage(
                httpCode = 200,
                message = null,
                exceptionName = null
            )
        } catch (e: JsonMappingException) {
            throw InvalidDeviceSetupJsonException("Provided setup config is invalid: ${e.message}")
        } catch (e: JsonParseException) {
            throw InvalidDeviceSetupJsonException("Provided setup config is invalid: ${e.message}")
        }
    }

    private fun getDestructuredTopic(topic: String): MatchResult.Destructured {
        return deviceSetupTopicRegex.find(topic)!!.destructured
    }
}
