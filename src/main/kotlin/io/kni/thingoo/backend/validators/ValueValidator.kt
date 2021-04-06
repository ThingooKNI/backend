package io.kni.thingoo.backend.validators

interface ValueValidator {
    fun isValid(value: String): Boolean
}
