package io.kni.thingoo.backend.readings.validators

import io.kni.thingoo.backend.utils.StringUtils

class BooleanReadingValueValidator : ReadingValueValidator {
    override fun isValid(value: String): Boolean {
        return StringUtils.isBoolean(value)
    }
}
