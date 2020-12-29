package io.kni.thingoo.backend.users

import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByUsername(username: String): Optional<User>
}
