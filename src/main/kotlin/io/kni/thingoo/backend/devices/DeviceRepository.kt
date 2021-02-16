package io.kni.thingoo.backend.devices

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : PagingAndSortingRepository<Device, Int>
