package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.mqtt.MqttMessage
import io.kni.thingoo.backend.mqtt.MqttMessageHandler
import org.springframework.stereotype.Service

@Service
class NewReadingMqttMessageHandler(
    private val readingService: ReadingService
) : MqttMessageHandler {

    companion object {
        val entityNewReadingTopicRegex = Regex("^/devices/(\\w+)/entities/(\\w+)/reading$")
    }

    override fun handle(message: String, topic: String): MqttMessage? {
        val (deviceKey, entityKey) = getDestructuredTopic(topic)

        return null
    }

    private fun getDestructuredTopic(topic: String): MatchResult.Destructured {
        return entityNewReadingTopicRegex.find(topic)!!.destructured
    }
}
