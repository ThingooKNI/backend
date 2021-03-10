package io.kni.thingoo.backend.icons

import org.springframework.stereotype.Service

@Service
class MaterialIconServiceImpl : MaterialIconService {

    override fun getIcons(): List<MaterialIcon> {
        return MaterialIcon.values().toList()
    }
}
