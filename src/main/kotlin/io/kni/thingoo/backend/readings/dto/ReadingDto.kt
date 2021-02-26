package io.kni.thingoo.backend.readings.dto

import java.time.LocalDateTime

data class ReadingDto(
    val id: Int,
    val value: String,
    val timestamp: LocalDateTime
)
