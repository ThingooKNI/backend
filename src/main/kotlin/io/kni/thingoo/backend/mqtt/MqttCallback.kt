package io.kni.thingoo.backend.mqtt

import com.fasterxml.jackson.databind.ObjectMapper
import io.kni.thingoo.backend.devices.DeviceSetupMqttMessageHandler
import io.kni.thingoo.backend.exceptions.RestException
import io.kni.thingoo.backend.readings.NewReadingMqttMessageHandler
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class MqttCallback(
    private val deviceSetupMqttMessageHandler: DeviceSetupMqttMessageHandler,
    private val newReadingMqttMessageHandler: NewReadingMqttMessageHandler,
    private val objectMapper: ObjectMapper,
) : MqttCallback {

    @Lazy
    @Autowired
    private lateinit var mqttService: MqttService

    companion object {
        private val logger = LoggerFactory.getLogger(MqttCallback::class.java)
    }

    override fun connectionLost(throwable: Throwable) {
        logger.info("[MQTT] Connection to broker lost, trying to reconnect...")
        mqttService.connectToBroker()
    }

    override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
        logger.info("[MQTT] Received message on topic $topic, data: ${String(mqttMessage.payload)}")
        try {
            handleMessage(String(mqttMessage.payload), topic)
        } catch (e: io.kni.thingoo.backend.mqtt.exceptions.MqttException) {
            logger.info("[MQTT] MqttException thrown, sending response", e)
            sendResponse(e.toMqttMessage(), topic, mqttMessage.qos)
        } catch (e: RestException) {
            logger.info("[MQTT] RestException thrown, sending response", e)
            sendResponse(e.toMqttMessage(), topic, mqttMessage.qos)
        }
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
        logger.info("[MQTT] Sent message to topic ${iMqttDeliveryToken.topics.getOrNull(0)}")
    }

    fun handleMessage(messagePayload: String, topic: String) {
        when {
            DeviceSetupMqttMessageHandler.deviceSetupTopicRegex.matches(topic) -> deviceSetupMqttMessageHandler.handle(messagePayload, topic)
            NewReadingMqttMessageHandler.entityNewReadingTopicRegex.matches(topic) -> newReadingMqttMessageHandler.handle(messagePayload, topic)
        }
    }

    private fun sendResponse(mqttMessage: io.kni.thingoo.backend.mqtt.MqttMessage, baseTopic: String, qos: Int = 1) {
        val messageJson = objectMapper.writeValueAsString(mqttMessage)
        val responseTopic = "$baseTopic/response"

        mqttService.publish(messageJson, responseTopic, qos, false) // should we really use QoS 1?
    }
}
