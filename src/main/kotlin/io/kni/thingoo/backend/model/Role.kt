package io.kni.thingoo.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id: Long,

    @Enumerated(EnumType.STRING)
    var name: RoleName,

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonIgnore
    var users: MutableSet<User> = mutableSetOf()
)
