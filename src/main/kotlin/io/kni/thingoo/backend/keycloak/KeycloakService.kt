package io.kni.thingoo.backend.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class KeycloakService {

    @Value("\${keycloak.auth-server-url}")
    private lateinit var keycloakServerUrl: String

    @Value("\${keycloak.realm}")
    private lateinit var keycloakRealm: String

    @Value("\${keycloak.resource}")
    private lateinit var keycloakClientId: String

    @Value("\${app.keycloak.admin-user.username}")
    private lateinit var keycloakAdminUsername: String

    @Value("\${app.keycloak.admin-user.password}")
    private lateinit var keycloakAdminPassword: String

    fun getInstance(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakServerUrl)
            .realm("master")
            .username(keycloakAdminUsername)
            .password(keycloakAdminPassword)
            .clientId("admin-cli")
            .build()
    }
}
