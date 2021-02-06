package io.kni.thingoo.backend.roles

import com.fasterxml.jackson.annotation.JsonIgnore
import io.kni.thingoo.backend.users.User

class Role(
    val id: Long,

    var name: RoleName,

    @JsonIgnore
    var users: MutableSet<User> = mutableSetOf()
)
