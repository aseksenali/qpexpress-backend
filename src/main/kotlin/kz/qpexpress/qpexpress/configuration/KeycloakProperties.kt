package kz.qpexpress.qpexpress.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "keycloak")
data class KeycloakProperties(
    val realm: String,
    val authServerUrl: String,
    val adminUsername: String,
    val adminPassword: String,
)