package io.kni.thingoo.backend.devices.exceptions

import io.kni.thingoo.backend.exceptions.RestException
import org.springframework.http.HttpStatus

class InvalidMACAddressException(msg: String) : RestException(msg, HttpStatus.BAD_REQUEST)
