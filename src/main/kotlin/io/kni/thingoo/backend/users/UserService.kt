package io.kni.thingoo.backend.users

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun getOneById(id: UUID): User {
        return userRepository.findById(id)
    }
}
