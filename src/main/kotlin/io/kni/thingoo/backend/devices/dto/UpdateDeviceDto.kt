package io.kni.thingoo.backend.devices.dto

import io.kni.thingoo.backend.icons.MaterialIcon

data class UpdateDeviceDto(
    val displayName: String?,
    val icon: MaterialIcon?
)
