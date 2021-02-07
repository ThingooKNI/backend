package io.kni.thingoo.backend.users

import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
interface UserRepository {

    fun findByUsername(username: String): Optional<User>

    fun findAll(): List<UserRepresentation>

    fun findById(id: UUID): Optional<User>

    fun save(user: User): User
}
