package io.kni.thingoo.backend.entities

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface EntityRepository : PagingAndSortingRepository<Entity, Int> {

    fun findByDeviceId(id: Int): List<Entity>
}
