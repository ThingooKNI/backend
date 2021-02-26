package io.kni.thingoo.backend.readings.validators

import io.kni.thingoo.backend.utils.StringUtils

class DecimalReadingValueValidator : ReadingValueValidator {
    override fun isValid(value: String): Boolean {
        return StringUtils.isDecimal(value)
    }
}
