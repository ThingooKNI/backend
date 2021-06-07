package io.kni.thingoo.backend.readings.exceptions

import io.kni.thingoo.backend.exceptions.RestException
import org.springframework.http.HttpStatus

class ReadingValueTypeMismatchException(msg: String) : RestException(msg, HttpStatus.BAD_REQUEST)
