package io.kni.thingoo.backend.devices.exceptions

import io.kni.thingoo.backend.exceptions.RestException
import org.springframework.http.HttpStatus

class DeviceNotFoundException(msg: String) : RestException(msg, HttpStatus.NOT_FOUND)
