package io.kni.thingoo.backend.keycloak

import org.keycloak.KeycloakPrincipal
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class KeycloakService {

    @Value("\${keycloak.auth-server-url}")
    private lateinit var keycloakServerUrl: String

    @Value("\${keycloak.realm}")
    private lateinit var keycloakRealm: String

    fun getInstance(): Keycloak? {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication.principal is KeycloakPrincipal<*>) {
            val principal = authentication.principal as KeycloakPrincipal<*>

            val keycloakSecurityContext = principal.keycloakSecurityContext

            return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm(keycloakRealm)
                .authorization(keycloakSecurityContext.tokenString)
                .build()
        }

        return null
    }
}
