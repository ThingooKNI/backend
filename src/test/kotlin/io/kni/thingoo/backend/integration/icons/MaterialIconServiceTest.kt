package io.kni.thingoo.backend.integration.icons

import io.kni.thingoo.backend.icons.MaterialIcon
import io.kni.thingoo.backend.icons.MaterialIconService
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class MaterialIconServiceTest {

    @Autowired
    private lateinit var iconService: MaterialIconService

    @Test
    fun `given MaterialIcon enum, when getting all icons, will return all enum values`() {
        // given

        // when
        val icons = iconService.getIcons()

        // then
        assertThat(icons).containsExactlyElementsOf(MaterialIcon.values().toList())
    }
}
