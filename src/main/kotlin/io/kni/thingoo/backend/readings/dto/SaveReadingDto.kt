package io.kni.thingoo.backend.readings.dto

data class SaveReadingDto(
    val value: String,
    val entityKey: String,
    val deviceKey: String
)
