package io.kni.thingoo.backend.mqtt

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MqttMessageHandler : MqttCallback {

    var mqttPushClient: MqttPushClient? = null

    private val logger = LoggerFactory.getLogger(MqttPushClient::class.java)

    override fun connectionLost(throwable: Throwable) {
        logger.info("Disconnected, can be reconnected")
        mqttPushClient?.setup()
    }

    override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
        logger.info("Received message topic: $topic")
        logger.info("Received message Qos: ${mqttMessage.qos}")
        logger.info("Received message content: ${String(mqttMessage.payload)}")
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
        logger.info("Sent" + iMqttDeliveryToken.isComplete)
    }
}
