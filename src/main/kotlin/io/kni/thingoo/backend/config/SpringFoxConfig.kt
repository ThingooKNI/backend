package io.kni.thingoo.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.AuthorizationCodeGrantBuilder
import springfox.documentation.builders.OAuthBuilder
import springfox.documentation.builders.OperationBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.Contact
import springfox.documentation.service.GrantType
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.SecurityScheme
import springfox.documentation.service.TokenEndpoint
import springfox.documentation.service.TokenRequestEndpoint
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.spi.service.contexts.OperationModelContextsBuilder
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder

@Configuration
class SpringFoxConfig {

    @Value("\${app.keycloak.external-auth-server-url}")
    lateinit var authServerUrl: String

    @Value("\${app.keycloak.webapp-client-id}")
    lateinit var webappClientId: String

    @Value("\${app.keycloak.webapp-client-secret}")
    lateinit var webappClientSecret: String

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(listOf(securityScheme()))
            .securityContexts(listOf(securityContext()))
            .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "Thingoo REST API",
            "",
            "0.0.8",
            null,
            Contact("Bartłomiej Rasztabiga", "https://github.com/BartlomiejRasztabiga", "contact@rasztabiga.me"),
            null, null, emptyList()
        )
    }

    @Bean
    fun security(): SecurityConfiguration {
        return SecurityConfigurationBuilder.builder()
            .clientId(webappClientId)
            .clientSecret(webappClientSecret)
            .scopeSeparator(" ")
            .useBasicAuthenticationWithAccessCodeGrant(false)
            .build()
    }

    private fun securityScheme(): SecurityScheme {
        val grantType: GrantType = AuthorizationCodeGrantBuilder()
            .tokenEndpoint { TokenEndpoint("$authServerUrl/realms/Thingoo/protocol/openid-connect/token",
                "oauthtoken") }
            .tokenRequestEndpoint {
                TokenRequestEndpoint(
                    "$authServerUrl/realms/Thingoo/protocol/openid-connect/auth", webappClientId, webappClientSecret
                )
            }
            .build()
        return OAuthBuilder().name("spring_oauth")
            .grantTypes(listOf(grantType))
            .scopes(scopes())
            .build()
    }

    private fun scopes(): List<AuthorizationScope> {
        // TODO
        return listOf()
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder()
            .securityReferences(
                listOf(SecurityReference("spring_oauth", scopes().toTypedArray()))
            )
            .forPaths(PathSelectors.regex("/api.*"))
            .build()
    }
}
