package io.kni.thingoo.backend.mqtt

interface MqttPubSubClient {

    fun connect(mqttCallback: MqttCallback)

    fun publish(pushMessage: String, topic: String, qos: Int, retain: Boolean)

    fun subscribeToDefaultTopic()
}
