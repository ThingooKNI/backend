package io.kni.thingoo.backend.mqtt

interface MqttMessageHandler {
    fun handle(message: String, topic: String)
}
