package io.kni.thingoo.backend.devices

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.kni.thingoo.backend.devices.dto.RegisterDeviceDto
import io.kni.thingoo.backend.devices.exceptions.InvalidDeviceSetupJsonException
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

    override fun handle(message: String, topic: String) {
        val (deviceKey) = getDestructuredTopic(topic)

        try {
            val registerDeviceDto = objectMapper.readValue(message, RegisterDeviceDto::class.java)
            // TODO return status object (200 OK or exception)
        } catch (e: JsonMappingException) {
            throw InvalidDeviceSetupJsonException("Provided setup config is invalid: ${e.message}")
        }
    }

    private fun getDestructuredTopic(topic: String): MatchResult.Destructured {
        return deviceSetupTopicRegex.find(topic)!!.destructured
    }
}
