package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.devices.DeviceService
import io.kni.thingoo.backend.mqtt.MqttMessageHandler
import org.springframework.stereotype.Service

@Service
class NewReadingMqttMessageHandler(
    private val readingService: ReadingService
) : MqttMessageHandler {

    override fun handle(message: String, topic: String) {
        TODO("Not yet implemented")
    }
}
