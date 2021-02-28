package io.kni.thingoo.backend.entities.exceptions

import io.kni.thingoo.backend.exceptions.RestException
import org.springframework.http.HttpStatus

class EntityNotFoundException(msg: String) : RestException(msg, HttpStatus.NOT_FOUND)
