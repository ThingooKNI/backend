package io.kni.thingoo.backend.mqtt

import io.kni.thingoo.backend.config.MqttConfig
import io.kni.thingoo.backend.utils.StringUtils
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.LoggerFactory
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

@Component
@Profile("production")
class MqttPubSubClientImpl(
    private val config: MqttConfig,
    private val ctx: ConfigurableApplicationContext
) : MqttPubSubClient {

    companion object {
        private val logger = LoggerFactory.getLogger(MqttPubSubClientImpl::class.java)
    }

    private var client: MqttAsyncClient? = null

    override fun connect(mqttCallback: MqttCallback) {
        val mqttClient = createMqttClient()

        mqttClient.setCallback(mqttCallback)

        this.client = mqttClient

        connectAsync()
    }

    override fun publish(pushMessage: String, topic: String, qos: Int, retain: Boolean) {
        val message = createMqttMessage(pushMessage, qos, retain)

        publishAsync(topic, message)
    }

    override fun subscribeToDefaultTopic() {
        logger.info("[MQTT] Started subscription on topic: ${config.defaultTopic}")
        client!!.subscribe(config.defaultTopic, 2)
    }

    private fun createMqttClient(): MqttAsyncClient {
        return MqttAsyncClient(config.hostUrl, getClientID(), MemoryPersistence())
    }

    private fun getClientID(): String {
        return "${config.clientID}-${StringUtils.getRandomAlphanumericString(8)}"
    }

    private fun connectAsync() {
        val options = createMqttConnectOptions()
        tryConnectAsyncWithRetries(options)
    }

    private fun tryConnectAsyncWithRetries(options: MqttConnectOptions) {
        var retries = 0
        while (true) {
            if (retries == config.connectRetryCount) {
                logger.error("[MQTT] Couldn't connect to broker in $retries retries. Closing Spring application.")
                ctx.close()
                exitProcess(-1)
            }

            val token = client!!.connect(options)
            val timeoutInMillis = TimeUnit.SECONDS.toMillis(config.connectTimeout)
            try {
                retries++
                logger.warn("[MQTT] Trying to connect to broker")
                token.waitForCompletion(timeoutInMillis)
            } catch (ignore: MqttException) {
                TimeUnit.SECONDS.sleep(config.connectTimeout)
            }

            if (client!!.isConnected) {
                logger.info("[MQTT] Connected to broker in $retries retries")
                break
            }
        }
    }

    private fun createMqttConnectOptions(): MqttConnectOptions {
        val options = MqttConnectOptions()
        options.isCleanSession = true
        options.userName = config.username
        options.password = config.password.toCharArray()
        options.connectionTimeout = config.timeout
        options.keepAliveInterval = config.keepalive

        return options
    }

    private fun createMqttMessage(messagePayload: String, qos: Int, retain: Boolean): MqttMessage {
        val message = MqttMessage()
        message.qos = qos
        message.isRetained = retain
        message.payload = messagePayload.toByteArray()

        return message
    }

    private fun publishAsync(topic: String, message: MqttMessage) {
        val token = client!!.publish(topic, message)
        token.waitForCompletion()
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
