package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.mqtt.MqttMessageHandler
import org.springframework.stereotype.Service

@Service
class DeviceSetupMqttMessageHandler(
    private val deviceService: DeviceService
) : MqttMessageHandler {

    override fun handle(message: String, topic: String) {
        TODO("Not yet implemented")
    }
}
