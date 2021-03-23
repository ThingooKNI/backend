package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.mqtt.MqttMessageHandler
import io.kni.thingoo.backend.readings.dto.SaveReadingDto
import org.springframework.stereotype.Component

@Component
class NewReadingMqttMessageHandler(
    private val readingService: ReadingService
) : MqttMessageHandler {

    companion object {
        val entityNewReadingTopicRegex = Regex("^/devices/(\\w+)/entities/(\\w+)/reading$")
    }

    override fun handle(message: String, topic: String) {
        val (deviceKey, entityKey) = getKeysFromTopic(topic)

        val reading = SaveReadingDto(
            value = message,
            entityKey = entityKey,
            deviceKey = deviceKey
        )
        readingService.createReading(reading)
    }

    private fun getKeysFromTopic(topic: String): MatchResult.Destructured {
        return entityNewReadingTopicRegex.find(topic)!!.destructured
    }
}
