package io.kni.thingoo.backend.commands

import io.kni.thingoo.backend.commands.dto.NewCommandDto
import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.entities.EntityRepository
import io.kni.thingoo.backend.entities.EntityType
import io.kni.thingoo.backend.exceptions.ApiErrorCode
import io.kni.thingoo.backend.mqtt.MqttService
import org.springframework.stereotype.Service

@Service
class CommandServiceImpl(
    private val entityRepository: EntityRepository,
    private val mqttService: MqttService
) : CommandService {

    override fun sendNewCommandToEntity(command: NewCommandDto, entityId: Int) {
        val entityOptional = entityRepository.findById(entityId)
        if (entityOptional.isEmpty) {
            ApiErrorCode.ENTITIES_003.throwException()
        }

        val entity = entityOptional.get()

        if (entity.type != EntityType.ACTUATOR) {
            ApiErrorCode.COMMANDS_001.throwException()
        }

        val commandTopic = getCommandTopic(entity)
        val mqttMessage = command.value
        mqttService.publish(mqttMessage, commandTopic, 1, false) // should we use qos 2 instead? Might not be supported by clients
    }

    private fun getCommandTopic(entity: Entity): String {
        val entityKey = entity.key
        val deviceKey = entity.device!!.key

        return "/devices/$deviceKey/entities/$entityKey/command"
    }
}
