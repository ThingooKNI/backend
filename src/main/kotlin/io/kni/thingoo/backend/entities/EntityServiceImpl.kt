package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.entities.dto.PatchEntityDto
import io.kni.thingoo.backend.entities.dto.UpdateEntityDto
import io.kni.thingoo.backend.exceptions.ApiErrorCode
import io.kni.thingoo.backend.utils.PatchUtils
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

    override fun updateEntity(id: Int, updateEntityDto: UpdateEntityDto): EntityDto {
        val entityOptional = entityRepository.findById(id)
        if (entityOptional.isEmpty) {
            ApiErrorCode.ENTITIES_003.throwException()
        }

        val entity = entityOptional.get()
        entity.displayName = updateEntityDto.displayName
        entity.icon = updateEntityDto.icon
        return entityRepository.save(entity).toDto()
    }

    override fun patchEntity(id: Int, patch: Map<String, Any>): EntityDto {
        val entityOptional = entityRepository.findById(id)
        if (entityOptional.isEmpty) {
            ApiErrorCode.ENTITIES_003.throwException()
        }

        val entity = entityOptional.get()
        try {
            PatchUtils.patchEntity(patch, PatchEntityDto::class, entity)
        } catch (e: Exception) {
            ApiErrorCode.ENTITIES_004.throwException()
        }

        return entityRepository.save(entity).toDto()
    }
}
