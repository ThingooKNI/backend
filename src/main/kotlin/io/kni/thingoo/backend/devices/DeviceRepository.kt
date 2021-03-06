package io.kni.thingoo.backend.devices

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DeviceRepository : PagingAndSortingRepository<Device, Int> {

    fun findByKey(key: String): Optional<Device>

    fun findByMacAddress(macAddress: String): Optional<Device>
}
