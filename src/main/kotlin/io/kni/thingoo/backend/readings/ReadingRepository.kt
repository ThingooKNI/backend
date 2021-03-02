package io.kni.thingoo.backend.readings

import org.springframework.data.repository.PagingAndSortingRepository
import java.util.Optional

interface ReadingRepository : PagingAndSortingRepository<Reading, Int> {

    fun findByEntityId(id: Int): List<Reading>

    fun findByEntityKeyAndEntityDeviceKey(entityKey: String, entityDeviceKey: String): List<Reading>

    fun findFirstByEntityIdOrderByTimestampDesc(id: Int): Optional<Reading>

    fun findFirstByEntityKeyAndEntityDeviceKeyOrderByTimestampDesc(entityKey: String, entityDeviceKey: String): Optional<Reading>
}
