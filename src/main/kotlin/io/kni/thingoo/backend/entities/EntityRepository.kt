package io.kni.thingoo.backend.entities

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface EntityRepository : PagingAndSortingRepository<Entity, Int> {

    fun findByDeviceId(id: Int): List<Entity>

    fun findByKeyAndDeviceKey(key: String, deviceKey: String): Optional<Entity>
}
