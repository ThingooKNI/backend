package io.kni.thingoo.backend.readings.validators

class StringReadingValueValidator : ReadingValueValidator {
    override fun isValid(value: String): Boolean {
        return true
    }
}
