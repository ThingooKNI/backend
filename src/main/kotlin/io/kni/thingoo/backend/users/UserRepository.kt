package io.kni.thingoo.backend.users

import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
interface UserRepository {

    fun findByUsername(username: String): Optional<User>

    fun findAll(): List<User>

    fun findById(id: UUID): Optional<User>

    fun save(user: User): User
}
