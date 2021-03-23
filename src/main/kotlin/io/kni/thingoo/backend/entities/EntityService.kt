package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.entities.dto.UpdateEntityDto

interface EntityService {

    fun getEntitiesByDeviceId(deviceId: Int): List<EntityDto>

    fun getEntityById(id: Int): EntityDto

    fun updateEntityById(id: Int, updateEntityDto: UpdateEntityDto): EntityDto

    fun patchEntityById(id: Int, patch: Map<String, Any>): EntityDto
}
