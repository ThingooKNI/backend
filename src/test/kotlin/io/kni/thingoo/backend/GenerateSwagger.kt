package io.kni.thingoo.backend

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.ws.rs.core.MediaType

@SpringBootTest
@AutoConfigureEmbeddedDatabase
class GenerateSwagger {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Test
    @Throws(Exception::class)
    fun generateSwagger() {
        val mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        mockMvc
            .perform(MockMvcRequestBuilders.get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
            .andDo { result: MvcResult ->
                FileUtils.writeStringToFile(
                    File("swagger.json"),
                    result.response.contentAsString,
                    StandardCharsets.UTF_8
                )
            }
    }
}
