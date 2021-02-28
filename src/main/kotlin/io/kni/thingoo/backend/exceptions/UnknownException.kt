package io.kni.thingoo.backend.exceptions

import org.springframework.http.HttpStatus

class UnknownException : RestException("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR)
