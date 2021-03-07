package io.kni.thingoo.backend.mqtt

import io.kni.thingoo.backend.config.MqttConfig
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MqttPushClient(
    private val mqttCallback: MqttCallback,
    private val mqttConfig: MqttConfig
) {

    private val logger = LoggerFactory.getLogger(MqttPushClient::class.java)

    private var client: MqttClient? = null

    init {
        this.setup()
    }

    fun setup() {
        this.connect(mqttConfig)
        this.subscribe(mqttConfig.defaultTopic, 0)
    }

    fun connect(config: MqttConfig) {
        val client = MqttClient(config.hostUrl, config.clientID, MemoryPersistence())

        val options = MqttConnectOptions()
        options.isCleanSession = true
        options.userName = config.username
        options.password = config.password.toCharArray()
        options.connectionTimeout = config.timeout
        options.keepAliveInterval = config.keepalive

        mqttCallback.mqttPushClient = this
        client.setCallback(mqttCallback)
        client.connect(options)
        this.client = client
    }

    fun publish(pushMessage: String, topic: String, qos: Int, retain: Boolean) {
        val message = MqttMessage()
        message.qos = qos
        message.isRetained = retain
        message.payload = pushMessage.toByteArray()

        val mqttTopic = client!!.getTopic(topic)

        if (null == mqttTopic) {
            logger.error("topic not exist")
        }
        val token = mqttTopic!!.publish(message)
        token.waitForCompletion()
    }

    fun subscribe(topic: String, qos: Int) {
        logger.info("Start subscribing to topics$topic")
        client!!.subscribe(topic, qos)
    }
}
