package io.kni.thingoo.backend.mqtt

interface MqttService {

    fun connectToBroker()

    fun publish(message: String, topic: String, qos: Int, retain: Boolean)
}
