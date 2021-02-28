package io.kni.thingoo.backend.entities.exceptions

import io.kni.thingoo.backend.exceptions.RestException
import org.springframework.http.HttpStatus

class ExistingEntityKeyException(msg: String) : RestException(msg, HttpStatus.CONFLICT)
