package io.kni.thingoo.backend.users

import io.kni.thingoo.backend.keycloak.KeycloakService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class UserRepositoryImpl(private val keycloakService: KeycloakService) : UserRepository {



    override fun findByUsername(username: String): Optional<User> {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<User> {

        val keycloakInstance = keycloakService.getInstance()

        println(keycloakInstance)

        return listOf()
    }

    override fun findById(id: UUID): Optional<User> {
        TODO("Not yet implemented")
    }

    override fun save(user: User): User {
        TODO("Not yet implemented")
    }
}
