package io.kni.thingoo.backend.readings.validators

interface ReadingValueValidator {
    fun isValid(value: String): Boolean
}
