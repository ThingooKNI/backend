package io.kni.thingoo.backend.users

import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun getOneById(id: UUID): Optional<User> {
        return userRepository.findById(id)
    }

    fun getOneByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }

    fun createNew(userCreateDto: UserCreateDto): User {
        TODO("Not yet implemented")
    }

    fun updateOne() {
        TODO("Not yet implemented")
    }
}
