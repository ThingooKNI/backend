package io.kni.thingoo.backend.repository

import io.kni.thingoo.backend.model.User
import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, UUID> {

    fun findByUsername(username: String): User
}