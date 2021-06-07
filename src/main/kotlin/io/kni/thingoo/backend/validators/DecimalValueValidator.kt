package io.kni.thingoo.backend.validators

import io.kni.thingoo.backend.utils.StringUtils

class DecimalValueValidator : ValueValidator {
    override fun isValid(value: String): Boolean {
        return StringUtils.isDecimal(value)
    }
}
