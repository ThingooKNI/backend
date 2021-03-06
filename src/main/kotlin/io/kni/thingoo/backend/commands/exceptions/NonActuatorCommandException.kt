package io.kni.thingoo.backend.commands.exceptions

import io.kni.thingoo.backend.exceptions.RestException
import org.springframework.http.HttpStatus

class NonActuatorCommandException(msg: String) : RestException(msg, HttpStatus.BAD_REQUEST)
