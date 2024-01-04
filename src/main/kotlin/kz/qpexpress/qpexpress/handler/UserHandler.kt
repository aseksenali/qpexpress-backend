package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.configuration.KeycloakProperties
import org.keycloak.admin.client.Keycloak
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class UserHandler(
    keycloak: Keycloak,
    keycloakProperties: KeycloakProperties,
) : IUserHandler {
    private val realm = keycloak.realm(keycloakProperties.realm)
    override fun getMyInformation(id: UUID): ServerResponse {
        val user = realm.users()[id.toString()].toRepresentation()
        return ServerResponse.ok().body(user)
    }

    @PreAuthorize("hasAuthority('users:read')")
    override fun getAll(): ServerResponse {
        val users = realm.users().list()
        return ServerResponse.ok().body(users)
    }
}