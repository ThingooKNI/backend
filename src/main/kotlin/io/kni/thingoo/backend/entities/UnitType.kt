package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.readings.validators.BooleanReadingValueValidator
import io.kni.thingoo.backend.readings.validators.DecimalReadingValueValidator
import io.kni.thingoo.backend.readings.validators.IntegerReadingValueValidator
import io.kni.thingoo.backend.readings.validators.ReadingValueValidator

enum class UnitType {
    INTEGER,
    DECIMAL,
    STRING,
    BOOLEAN;

    fun getReadingValueValidator(): ReadingValueValidator {
        return when {
            this == INTEGER -> {
                IntegerReadingValueValidator()
            }
            this == DECIMAL -> {
                DecimalReadingValueValidator()
            }
            this == BOOLEAN -> {
                BooleanReadingValueValidator()
            }
            this == STRING -> {
                IntegerReadingValueValidator()
            }
            else -> throw IllegalStateException("Invalid UnitType value")
        }
    }
}
