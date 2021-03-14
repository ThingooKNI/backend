package io.kni.thingoo.backend.users

import org.springframework.stereotype.Component
import java.util.UUID

@Component
interface UserRepository {
    fun findAll(): List<User>

    fun findById(id: UUID): User
}
