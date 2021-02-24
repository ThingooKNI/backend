package io.kni.thingoo.backend.readings.dto

data class SaveReadingDto(
    var value: String,
    var entityKey: Int,
    var deviceId: Int
)
