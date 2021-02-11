package io.kni.thingoo.backend.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import java.util.stream.Collectors
import java.util.stream.Stream

class CustomJwtAuthenticationConverter(private val webappClientId: String, private val deviceClientId: String) :
    Converter<Jwt?, AbstractAuthenticationToken?> {

    private val defaultGrantedAuthoritiesConverter: JwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()

    override fun convert(source: Jwt): AbstractAuthenticationToken {

        val resourceRolesStream = Stream.concat(
            extractResourceRoles(source, webappClientId).stream(),
            extractResourceRoles(source, deviceClientId).stream()
        )

        val authorities: MutableSet<GrantedAuthority?>? = Stream.concat(
            defaultGrantedAuthoritiesConverter.convert(source)
                ?.stream(),
            resourceRolesStream
        )
            .collect(Collectors.toSet())
        return JwtAuthenticationToken(source, authorities)
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        private fun extractResourceRoles(jwt: Jwt, resourceId: String): Collection<GrantedAuthority> {
            val resourceAccess: Map<String, Any> = jwt.getClaim("resource_access")
            val resource: Map<String, Any> = resourceAccess[resourceId] as Map<String, Any>? ?: return emptyList()
            val resourceRoles = resource["roles"] as Collection<String>
            return resourceRoles.stream()
                .map { role -> SimpleGrantedAuthority("ROLE_$role") }
                .collect(Collectors.toSet())
        }
    }
}
