package io.kni.thingoo.backend.validators

import io.kni.thingoo.backend.utils.StringUtils

class IntegerValueValidator : ValueValidator {
    override fun isValid(value: String): Boolean {
        return StringUtils.isInteger(value)
    }
}
