package io.kni.thingoo.backend.mqtt

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MqttMessage(
    val message: String?,
    val httpCode: Int?,
    val exceptionName: String?
)
