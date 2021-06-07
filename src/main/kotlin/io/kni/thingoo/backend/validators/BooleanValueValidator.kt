package io.kni.thingoo.backend.validators

import io.kni.thingoo.backend.utils.StringUtils

class BooleanValueValidator : ValueValidator {
    override fun isValid(value: String): Boolean {
        return StringUtils.isBoolean(value)
    }
}
