package io.kni.thingoo.backend.commands

import io.kni.thingoo.backend.commands.dto.NewCommandDto

interface CommandService {

    fun sendNewCommandToEntity(command: NewCommandDto, entityId: Int)
}
