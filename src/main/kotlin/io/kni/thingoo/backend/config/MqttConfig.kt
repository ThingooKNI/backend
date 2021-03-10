package io.kni.thingoo.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("mqtt")
class MqttConfig {
    lateinit var username: String
    lateinit var password: String
    lateinit var hostUrl: String
    lateinit var clientID: String
    lateinit var defaultTopic: String
    var timeout: Int = 0
    var keepalive: Int = 0
}
