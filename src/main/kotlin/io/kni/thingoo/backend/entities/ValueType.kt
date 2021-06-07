package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.validators.BooleanValueValidator
import io.kni.thingoo.backend.validators.DecimalValueValidator
import io.kni.thingoo.backend.validators.IntegerValueValidator
import io.kni.thingoo.backend.validators.StringValueValidator
import io.kni.thingoo.backend.validators.ValueValidator

enum class ValueType {
    INTEGER,
    DECIMAL,
    STRING,
    BOOLEAN;

    fun getValueValidator(): ValueValidator {
        return when {
            this == INTEGER -> {
                IntegerValueValidator()
            }
            this == DECIMAL -> {
                DecimalValueValidator()
            }
            this == BOOLEAN -> {
                BooleanValueValidator()
            }
            this == STRING -> {
                StringValueValidator()
            }
            else -> throw IllegalStateException("Invalid ValueType value")
        }
    }
}
