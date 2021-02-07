package io.kni.thingoo.backend.users

import org.keycloak.KeycloakPrincipal
import org.keycloak.representations.AccessToken
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    @PreAuthorize("hasRole('user')")
    fun getCurrentUser(): ResponseEntity<AccessToken> {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication.principal is KeycloakPrincipal<*>) {
            val principal = authentication.principal as KeycloakPrincipal<*>

            val session = principal.keycloakSecurityContext
            val accessToken = session.token
            return ResponseEntity.ok(accessToken)
        }

        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    fun getAllUsers(): List<UserRepresentation>? {

        return userService.getAll()
    }
}
