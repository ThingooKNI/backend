package io.kni.thingoo.backend.users

import java.util.UUID

interface UserService {

    fun getAllUsers(): List<User>

    fun getUserById(id: UUID): User
}
