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
        println(message)
        val match = deviceSetupTopicRegex
    }
}
