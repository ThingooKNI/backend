package io.kni.thingoo.backend.users

data class UserCreateDto(val username: String, val rawPassword: String, val firstName: String, val lastName: String, val email: String)
