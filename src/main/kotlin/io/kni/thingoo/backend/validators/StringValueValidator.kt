package io.kni.thingoo.backend.validators

class StringValueValidator : ValueValidator {
    override fun isValid(value: String): Boolean {
        return true
    }
}
