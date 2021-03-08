package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.mqtt.MqttMessageHandler
import org.springframework.stereotype.Service

@Service
class DeviceSetupMqttMessageHandler(
    private val deviceService: DeviceService
) : MqttMessageHandler {

    companion object {
        val deviceSetupTopicRegex = Regex("^/devices/(\\w+)/setup$")
    }

    override fun handle(message: String, topic: String) {
        val (deviceKey) = getDestructuredTopic(topic)
    }

    private fun getDestructuredTopic(topic: String): MatchResult.Destructured {
        return deviceSetupTopicRegex.find(topic)!!.destructured
    }
}
