package io.kni.thingoo.backend.roles

import com.fasterxml.jackson.annotation.JsonIgnore
import io.kni.thingoo.backend.users.User
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

class Role(
    val id: Long,

    var name: RoleName,

    @JsonIgnore
    var users: MutableSet<User> = mutableSetOf()
)
