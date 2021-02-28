package io.kni.thingoo.backend.exceptions

import org.springframework.http.HttpStatus

open class RestException(msg: String, val status: HttpStatus) : Exception(msg) {
    var code: ErrorCode? = null
}
