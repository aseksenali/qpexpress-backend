package kz.qpexpress.qpexpress.configuration

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfig(
    private val keycloakProperties: KeycloakProperties
) {
    @Bean
    fun keycloak(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.authServerUrl)
            .realm("master")
            .clientId("admin-cli")
            .grantType(OAuth2Constants.PASSWORD)
            .username(keycloakProperties.adminUsername)
            .password(keycloakProperties.adminPassword)
            .build()
    }
}