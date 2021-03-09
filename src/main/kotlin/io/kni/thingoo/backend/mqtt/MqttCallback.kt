package io.kni.thingoo.backend.mqtt

import com.fasterxml.jackson.databind.ObjectMapper
import io.kni.thingoo.backend.devices.DeviceSetupMqttMessageHandler
import io.kni.thingoo.backend.exceptions.RestException
import io.kni.thingoo.backend.readings.NewReadingMqttMessageHandler
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MqttCallback(
    private val deviceSetupMqttMessageHandler: DeviceSetupMqttMessageHandler,
    private val newReadingMqttMessageHandler: NewReadingMqttMessageHandler,
    private val objectMapper: ObjectMapper
) : MqttCallback {

    var mqttPushClient: MqttPushClient? = null

    companion object {
        private val logger = LoggerFactory.getLogger(MqttPushClient::class.java)
    }

    override fun connectionLost(throwable: Throwable) {
        logger.info("MQTT connection to broker lost, trying to reconnect...")
        mqttPushClient?.setup()
    }

    override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
        logger.debug("Received MQTT message on topic $topic, date: ${String(mqttMessage.payload)}")
        try {
            val response = handleMessage(String(mqttMessage.payload), topic)
            if (response != null) {
                sendResponse(response, topic, mqttMessage.qos)
            }
        } catch (e: io.kni.thingoo.backend.mqtt.exceptions.MqttException) {
            logger.error("MqttException thrown, sending response", e)
            sendResponse(e.toMqttMessage(), topic, mqttMessage.qos)
        } catch (e: RestException) {
            logger.error("MqttException thrown, sending response", e)
            sendResponse(e.toMqttMessage(), topic, mqttMessage.qos)
        }
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
    }

    fun handleMessage(messagePayload: String, topic: String): io.kni.thingoo.backend.mqtt.MqttMessage? {
        return when {
            DeviceSetupMqttMessageHandler.deviceSetupTopicRegex.matches(topic) -> deviceSetupMqttMessageHandler.handle(messagePayload, topic)
            NewReadingMqttMessageHandler.entityNewReadingTopicRegex.matches(topic) -> newReadingMqttMessageHandler.handle(messagePayload, topic)
            else -> null
        }
    }

    private fun sendResponse(mqttMessage: io.kni.thingoo.backend.mqtt.MqttMessage, baseTopic: String, qos: Int = 1) {
        val messageJson = objectMapper.writeValueAsString(mqttMessage)
        val responseTopic = "$baseTopic/response"
        mqttPushClient?.publish(messageJson, responseTopic, qos, false) // should we really use QoS 1?
    }
}
