package io.kni.thingoo.backend.users

data class UserCreateDto(val username: String, val rawPassword: String, val fullName: String?)
