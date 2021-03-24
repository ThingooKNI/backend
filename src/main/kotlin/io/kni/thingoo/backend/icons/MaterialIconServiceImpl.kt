package io.kni.thingoo.backend.icons

import org.springframework.stereotype.Service

@Service
class MaterialIconServiceImpl : MaterialIconService {

    override fun getAllIcons(): List<MaterialIcon> {
        return MaterialIcon.values().toList()
    }
}
