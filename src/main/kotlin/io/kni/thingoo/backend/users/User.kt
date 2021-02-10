package io.kni.thingoo.backend.users

import org.keycloak.representations.idm.UserRepresentation
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val enabled: Boolean
) {

    companion object {
        fun fromUserRepresentation (userRepresentation: UserRepresentation): User {
            return User(
                id = UUID.fromString(userRepresentation.id),
                username = userRepresentation.username,
                firstName = userRepresentation.firstName,
                lastName = userRepresentation.lastName,
                email = userRepresentation.email,
                enabled = userRepresentation.isEnabled
            )
        }
    }
}
