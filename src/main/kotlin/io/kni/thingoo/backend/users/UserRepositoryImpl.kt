package io.kni.thingoo.backend.users

import io.kni.thingoo.backend.users.exceptions.NoViewUsersRoleException
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import java.net.URI
import java.util.Optional
import java.util.UUID

@Component
class UserRepositoryImpl(private val keycloakRestTemplate: KeycloakRestTemplate) : UserRepository {

    @Value("\${keycloak.auth-server-url}")
    private lateinit var keycloakServerUrl: String

    @Value("\${keycloak.realm}")
    private lateinit var keycloakRealm: String


    override fun findByUsername(username: String): Optional<User> {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<User> {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): Optional<User> {
        TODO("Not yet implemented")
    }

    override fun save(user: User): User {
        TODO("Not yet implemented")
    }
}
