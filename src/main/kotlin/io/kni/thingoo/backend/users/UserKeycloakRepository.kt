package io.kni.thingoo.backend.users

import io.kni.thingoo.backend.keycloak.KeycloakService
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class UserKeycloakRepository(private val keycloakService: KeycloakService) : UserRepository {

    @Value("\${keycloak.realm}")
    private lateinit var keycloakRealm: String

    override fun findByUsername(username: String): Optional<User> {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<UserRepresentation> {
        val keycloakInstance = keycloakService.getInstance() ?: return emptyList()

        return keycloakInstance.realm(keycloakRealm).users().list()
    }

    override fun findById(id: UUID): Optional<User> {
        TODO("Not yet implemented")
    }

    override fun save(user: User): User {
        TODO("Not yet implemented")
    }
}
