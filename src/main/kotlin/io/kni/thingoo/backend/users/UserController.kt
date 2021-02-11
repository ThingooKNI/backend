package io.kni.thingoo.backend.users

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<String> {
        //TODO Get current user from keycloak?

        return ResponseEntity.ok("")
    }

    @GetMapping
    fun getAllUsers(): List<User>? {
        return userService.getAll()
    }
}
