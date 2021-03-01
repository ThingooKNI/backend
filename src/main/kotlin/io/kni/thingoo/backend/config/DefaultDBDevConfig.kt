package io.kni.thingoo.backend.config

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PreDestroy
import javax.sql.DataSource

@Component
@Profile("dev")
class DefaultDBDevConfig {

    @Value("\${app.dev.database.port}")
    lateinit var port: String

    @Value("\${app.dev.database.dir}")
    lateinit var dir: String

    private lateinit var pg: EmbeddedPostgres

    @Bean
    @Profile("dev")
    fun defaultDataSource(): DataSource {
        pg = EmbeddedPostgres.builder()
            .setPort(port.toInt())
            .setDataDirectory(File(dir))
            .setCleanDataDirectory(false)
            .start()
        return pg.postgresDatabase
    }

    @PreDestroy
    private fun closeEmbeddedDatabase() {
        try {
            pg.postgresDatabase?.connection?.close()
            pg.close()
        } catch (e: Exception) {
        }
    }
}
