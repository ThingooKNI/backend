package io.kni.thingoo.backend.readings.exceptions

import io.kni.thingoo.backend.exceptions.RestException
import org.springframework.http.HttpStatus

class NoReadingsException(msg: String) : RestException(msg, HttpStatus.NOT_FOUND)
