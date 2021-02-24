package io.kni.thingoo.backend.readings

import org.springframework.data.repository.PagingAndSortingRepository

interface ReadingRepository : PagingAndSortingRepository<Reading, Int>
