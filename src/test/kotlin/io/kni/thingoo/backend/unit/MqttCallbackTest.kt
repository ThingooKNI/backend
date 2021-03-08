package io.kni.thingoo.backend.unit

import io.kni.thingoo.backend.devices.DeviceSetupMqttMessageHandler
import io.kni.thingoo.backend.mqtt.MqttCallback
import io.kni.thingoo.backend.readings.NewReadingMqttMessageHandler
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class MqttCallbackTest {

    private val deviceSetupMqttMessageHandlerMock = Mockito.mock(DeviceSetupMqttMessageHandler::class.java)
    private val newReadingMqttMessageHandlerMock = Mockito.mock(NewReadingMqttMessageHandler::class.java)

    @Test
    fun`given setup device topic, when handling mqtt message, will call DeviceSetupMqttMessageHandler`() {
        // given
        val mqttCallback = MqttCallback(deviceSetupMqttMessageHandlerMock, newReadingMqttMessageHandlerMock)
        val payload = ""
        val topic = "/devices/testDevice/setup"

        // when
        mqttCallback.handleMessage(payload, topic)

        // then
        Mockito.verify(deviceSetupMqttMessageHandlerMock).handle(payload, topic)
        Mockito.verifyNoInteractions(newReadingMqttMessageHandlerMock)
    }

    @Test
    fun`given new reading topic, when handling mqtt message, will call NewReadingMqttMessageHandler`() {
        // given
        val mqttCallback = MqttCallback(deviceSetupMqttMessageHandlerMock, newReadingMqttMessageHandlerMock)
        val payload = ""
        val topic = "/devices/testDevice/entities/temp/reading"

        // when
        mqttCallback.handleMessage(payload, topic)

        // then
        Mockito.verify(newReadingMqttMessageHandlerMock).handle(payload, topic)
        Mockito.verifyNoInteractions(deviceSetupMqttMessageHandlerMock)
    }

    @Test
    fun`given unknown topic, when handling mqtt message, will call none`() {
        // given
        val mqttCallback = MqttCallback(deviceSetupMqttMessageHandlerMock, newReadingMqttMessageHandlerMock)
        val payload = ""
        val topic = "/devices/testDevice/test"

        // when
        mqttCallback.handleMessage(payload, topic)

        // then
        Mockito.verifyNoInteractions(newReadingMqttMessageHandlerMock)
        Mockito.verifyNoInteractions(deviceSetupMqttMessageHandlerMock)
    }

    @Test
    fun`given unknown topic2, when handling mqtt message, will call none`() {
        // given
        val mqttCallback = MqttCallback(deviceSetupMqttMessageHandlerMock, newReadingMqttMessageHandlerMock)
        val payload = ""
        val topic = "/devices/testDevice/entities/temp/reading/test"

        // when
        mqttCallback.handleMessage(payload, topic)

        // then
        Mockito.verifyNoInteractions(newReadingMqttMessageHandlerMock)
        Mockito.verifyNoInteractions(deviceSetupMqttMessageHandlerMock)
    }

    @Test
    fun`given unknown topic3, when handling mqtt message, will call none`() {
        // given
        val mqttCallback = MqttCallback(deviceSetupMqttMessageHandlerMock, newReadingMqttMessageHandlerMock)
        val payload = ""
        val topic = "/devices/testDevice/setup/test"

        // when
        mqttCallback.handleMessage(payload, topic)

        // then
        Mockito.verifyNoInteractions(newReadingMqttMessageHandlerMock)
        Mockito.verifyNoInteractions(deviceSetupMqttMessageHandlerMock)
    }
}
