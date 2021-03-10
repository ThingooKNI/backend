package io.kni.thingoo.backend.exceptions

import io.kni.thingoo.backend.mqtt.MqttMessage
import org.springframework.http.HttpStatus

open class RestException(msg: String, val status: HttpStatus) : Exception(msg) {
    var code: ErrorCode? = null

    fun toMqttMessage(): MqttMessage {
        return MqttMessage(
            message = this.message,
            exceptionName = this.javaClass.simpleName,
            httpCode = this.status.value()
        )
    }
}
