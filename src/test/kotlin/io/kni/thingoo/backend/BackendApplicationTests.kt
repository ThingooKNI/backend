package io.kni.thingoo.backend

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureEmbeddedDatabase
class BackendApplicationTests {

    @Test
    fun contextLoads() {
    }
}
