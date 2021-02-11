package io.kni.thingoo.backend.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class KeycloakAdminClientConfig {

    @Value("\${app.keycloak.auth-server-url}")
    private lateinit var keycloakServerUrl: String

    @Value("\${app.keycloak.admin-username}")
    private lateinit var adminUsername: String

    @Value("\${app.keycloak.admin-password}")
    private lateinit var adminPassword: String

    @Bean
    fun keycloak(): Keycloak? {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakServerUrl)
            .realm("master")
            .clientId("admin-cli")
            .username(adminUsername)
            .password(adminPassword)
            .build()
    }
}
