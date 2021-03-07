package io.kni.thingoo.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Profile("!dev")
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Value("\${app.keycloak.webapp-client-id}")
    lateinit var webappClientId: String

    @Value("\${app.keycloak.device-client-id}")
    lateinit var deviceClientId: String

    override fun configure(http: HttpSecurity) {
        http.cors()
            .and()
            .authorizeRequests()
            .antMatchers("/v2/api-docs").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .mvcMatchers(HttpMethod.POST, "/devices").hasRole("device")
            .mvcMatchers(HttpMethod.POST, "/readings").hasRole("device")
            .anyRequest().hasRole("user")
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(CustomJwtAuthenticationConverter(webappClientId, deviceClientId))
    }
}

@Profile("dev")
@EnableWebSecurity
class NoSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/**")
    }
}
