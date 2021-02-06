package io.kni.thingoo.backend.users

import io.kni.thingoo.backend.roles.Role
import java.util.UUID

class User(
    val uuid: UUID = UUID.randomUUID(),

    var username: String,

    var password: String,

    var isActive: Boolean,

    var fullName: String?,

    var roles: MutableSet<Role> = mutableSetOf(),
)
