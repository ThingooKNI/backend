package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.entities.dto.EntityDto

interface EntityService {

    fun getEntities(deviceId: Int): List<EntityDto>

    fun getEntity(id: Int): EntityDto
}
