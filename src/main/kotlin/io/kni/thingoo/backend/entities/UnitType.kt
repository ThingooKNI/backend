package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.readings.validators.*

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
                StringReadingValueValidator()
            }
            else -> throw IllegalStateException("Invalid UnitType value")
        }
    }
}
