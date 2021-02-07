package io.kni.thingoo.backend.users

import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAll(): List<UserRepresentation> {
        return userRepository.findAll()
    }

    fun getOneById(id: UUID): Optional<User> {
        return userRepository.findById(id)
    }

    fun getOneByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }

    fun createNew(userCreateDto: UserCreateDto): User {
        // TODO hash password
        val hashedPassword = userCreateDto.rawPassword

        val newUser = User(
            username = userCreateDto.username,
            password = hashedPassword,
            fullName = userCreateDto.fullName,
            isActive = true
        )

        return userRepository.save(newUser)
    }

    fun updateOne() {
        throw NotImplementedError()
    }
}
