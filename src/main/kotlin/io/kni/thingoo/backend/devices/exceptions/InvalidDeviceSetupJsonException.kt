package io.kni.thingoo.backend.devices.exceptions

import io.kni.thingoo.backend.mqtt.exceptions.MqttException

class InvalidDeviceSetupJsonException(msg: String) : MqttException(msg)
