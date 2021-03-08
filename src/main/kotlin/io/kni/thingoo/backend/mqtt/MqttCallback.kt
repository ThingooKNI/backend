package io.kni.thingoo.backend.mqtt

import io.kni.thingoo.backend.devices.DeviceSetupMqttMessageHandler
import io.kni.thingoo.backend.readings.NewReadingMqttMessageHandler
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MqttCallback(
    private val deviceSetupMqttMessageHandler: DeviceSetupMqttMessageHandler,
    private val newReadingMqttMessageHandler: NewReadingMqttMessageHandler
) : MqttCallback {

    var mqttPushClient: MqttPushClient? = null

    companion object {
        private val logger = LoggerFactory.getLogger(MqttPushClient::class.java)

        private val deviceSetupTopicRegex = Regex("^/devices/(\\w+)/setup$")
        private val entityNewReadingTopicRegex = Regex("^/devices/(\\w+)/entities/(\\w+)/reading$")
    }


    override fun connectionLost(throwable: Throwable) {
        logger.info("Disconnected, can be reconnected")
        mqttPushClient?.setup()
    }

    override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
        logger.info("Received message topic: $topic")
        logger.info("Received message Qos: ${mqttMessage.qos}")
        logger.info("Received message content: ${String(mqttMessage.payload)}")

        handleMessage(mqttMessage.payload.toString(), topic)
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
        logger.info("Sent" + iMqttDeliveryToken.isComplete)
    }

    fun handleMessage(messagePayload: String, topic: String) {
        when {
            deviceSetupTopicRegex.matches(topic) -> deviceSetupMqttMessageHandler.handle(messagePayload, topic)
            entityNewReadingTopicRegex.matches(topic) -> newReadingMqttMessageHandler.handle(messagePayload, topic)
        }
    }
}
