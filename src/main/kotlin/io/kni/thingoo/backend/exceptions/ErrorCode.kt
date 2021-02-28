package io.kni.thingoo.backend.exceptions

interface ErrorCode {
    fun id(): String

    @Throws(RestException::class)
    fun throwException(): Nothing
}
