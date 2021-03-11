package io.kni.thingoo.backend.mqtt

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MqttServiceImpl(
    private val mqttPubSubClient: MqttPubSubClient,
    private val mqttCallback: MqttCallback
) : MqttService {
    init {
        this.connectToBroker()
    }

    override fun connectToBroker() {
        this.mqttPubSubClient.connect(mqttCallback)
        this.mqttPubSubClient.subscribeToDefaultTopic()
    }

    // has to be run on another thread to avoid blocking MqttCallback
    @Async
    override fun publish(message: String, topic: String, qos: Int, retain: Boolean) {
        this.mqttPubSubClient.publish(message, topic, qos, retain)
    }
}
