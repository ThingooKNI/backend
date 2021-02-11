package io.kni.thingoo.backend.users

import org.keycloak.admin.client.Keycloak
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class UserKeycloakRepository(private val keycloakInstance: Keycloak) : UserRepository {

    @Value("\${app.keycloak.realm}")
    private lateinit var keycloakRealm: String

    override fun findByUsername(username: String): User {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<User> {
        val users = keycloakInstance.realm(keycloakRealm).users().list()
        return users.map { User.fromUserRepresentation(it) }
    }

    override fun findById(id: UUID): User {
        val user = keycloakInstance.realm(keycloakRealm).users().get(id.toString()).toRepresentation()
        // This will probably throw some exception when not found

        return User.fromUserRepresentation(user)
    }

    override fun save(user: User): User {
        TODO("Not yet implemented")
    }
}
