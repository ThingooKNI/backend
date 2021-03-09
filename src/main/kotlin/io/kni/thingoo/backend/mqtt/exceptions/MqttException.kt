package io.kni.thingoo.backend.mqtt.exceptions

import io.kni.thingoo.backend.mqtt.MqttMessage

open class MqttException(msg: String) : Exception(msg) {
    fun toMqttMessage(): MqttMessage {
        return MqttMessage(
            message = this.message,
            exceptionName = this.javaClass.simpleName,
            httpCode = null
        )
    }
}
