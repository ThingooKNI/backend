package io.kni.thingoo.backend.readings

import org.springframework.data.repository.PagingAndSortingRepository

interface ReadingRepository : PagingAndSortingRepository<Reading, Int> {

    fun findByEntityId(id: Int): List<Reading>

    fun findByEntityKeyAndEntityDeviceKey(entityKey: String, entityDeviceKey: String): List<Reading>
}
