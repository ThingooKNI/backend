package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.exceptions.ApiErrorCode
import org.springframework.stereotype.Service

@Service
class EntityServiceImpl(
    private val entityRepository: EntityRepository
) : EntityService {
    override fun getEntities(deviceId: Int): List<EntityDto> {
        return entityRepository.findByDeviceId(deviceId).map { it.toDto() }
    }

    override fun getEntity(id: Int): EntityDto {
        val entityOptional = entityRepository.findById(id)
        return entityOptional
            .map { it.toDto() }
            .orElseThrow {
                ApiErrorCode.ENTITIES_003.throwException()
            }
    }
}
