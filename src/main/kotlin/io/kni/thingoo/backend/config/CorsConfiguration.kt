package io.kni.thingoo.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfiguration {

    @Value("#{'\${app.cors-origins}'.split(',')}")
    private lateinit var corsAllowedOrigins: List<String>

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry
                    .addMapping("/**")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedOrigins(*corsAllowedOrigins.toTypedArray())
                    .allowedHeaders("*")
                    .allowCredentials(true)
            }
        }
    }
}
