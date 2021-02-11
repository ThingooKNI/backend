package io.kni.thingoo.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity(debug = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Value("\${app.keycloak.webapp-client-id}")
    lateinit var webappClientId: String

    @Value("\${app.keycloak.device-client-id}")
    lateinit var deviceClientId: String

    override fun configure(http: HttpSecurity) {
        http.cors()
            .and()
            .authorizeRequests()
            .anyRequest().hasRole("user")
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(CustomJwtAuthenticationConverter(webappClientId, deviceClientId))
    }
}
