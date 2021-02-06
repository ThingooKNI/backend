package io.kni.thingoo.backend.users

import io.kni.thingoo.backend.roles.Role
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

class User(
    val uuid: UUID = UUID.randomUUID(),

    var username: String,

    var password: String,

    var isActive: Boolean,

    var fullName: String?,

    var roles: MutableSet<Role> = mutableSetOf(),
)
