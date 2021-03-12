package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.entities.dto.UpdateEntityDto

interface EntityService {

    fun getEntities(deviceId: Int): List<EntityDto>

    fun getEntity(id: Int): EntityDto

    fun updateEntity(updateEntityDto: UpdateEntityDto, id: Int): EntityDto
}
