package io.kni.thingoo.backend.users

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("users")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun getCurrentUser(principal: Principal): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getOneById(UUID.fromString(principal.name)))
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>?> {
        return ResponseEntity.ok(userService.getAll())
    }
}
