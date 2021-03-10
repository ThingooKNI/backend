package io.kni.thingoo.backend.mqtt

import io.kni.thingoo.backend.config.MqttConfig
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("production")
class MqttPubSubClientImpl(
    private val config: MqttConfig
) : MqttPubSubClient {

    companion object {
        private val logger = LoggerFactory.getLogger(MqttPubSubClientImpl::class.java)
    }

    private lateinit var client: MqttClient

    override fun connect(mqttCallback: MqttCallback) {
        val client = MqttClient(config.hostUrl, config.clientID, MemoryPersistence())

        val options = MqttConnectOptions()
        options.isCleanSession = true
        options.userName = config.username
        options.password = config.password.toCharArray()
        options.connectionTimeout = config.timeout
        options.keepAliveInterval = config.keepalive

        client.connect(options)
        client.setCallback(mqttCallback)

        this.client = client
    }

    override fun publish(pushMessage: String, topic: String, qos: Int, retain: Boolean) {
        val message = MqttMessage()
        message.qos = qos
        message.isRetained = retain
        message.payload = pushMessage.toByteArray()

        client.publish(topic, message)
//
//        val mqttTopic = client.getTopic(topic)
//
//        val token = mqttTopic.publish(message)
//        token.waitForCompletion()
    }

    override fun subscribeToDefaultTopic() {
        logger.info("[MQTT] Started subscription to topic: ${config.defaultTopic}")
        client.subscribe(config.defaultTopic, 2)
    }
}

@Component
@Profile("!production")
class MqttPubSubClientDevStub : MqttPubSubClient {

    override fun connect(mqttCallback: MqttCallback) {
        // do nothing
    }

    override fun publish(pushMessage: String, topic: String, qos: Int, retain: Boolean) {
        // do nothing
    }

    override fun subscribeToDefaultTopic() {
        // do nothing
    }
}
