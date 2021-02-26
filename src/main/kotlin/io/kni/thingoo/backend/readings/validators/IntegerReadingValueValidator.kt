package io.kni.thingoo.backend.readings.validators

import io.kni.thingoo.backend.utils.StringUtils

class IntegerReadingValueValidator : ReadingValueValidator {
    override fun isValid(value: String): Boolean {
        return StringUtils.isInteger(value)
    }
}
