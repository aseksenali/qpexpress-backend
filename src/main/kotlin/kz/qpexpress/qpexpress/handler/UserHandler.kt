package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.configuration.KeycloakProperties
import kz.qpexpress.qpexpress.dto.RecipientDTO
import kz.qpexpress.qpexpress.dto.UserDTO
import kz.qpexpress.qpexpress.repository.RecipientRepository
import org.keycloak.admin.client.Keycloak
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class UserHandler(
    keycloak: Keycloak,
    keycloakProperties: KeycloakProperties,
    private val recipientRepository: RecipientRepository,
) : IUserHandler {
    private val realm = keycloak.realm(keycloakProperties.realm)

    override fun getUserInformation(id: UUID): ServerResponse {
        val user = realm.users()[id.toString()].toRepresentation()
        return ServerResponse.ok().body(UserDTO.UserResponseDTO(user))
    }

    @PreAuthorize("hasAuthority('users:read')")
    override fun getAll(): ServerResponse {
        val groupId = realm.groups().groups().find { it.name == "User" }?.id ?: return ServerResponse.notFound().build()
        val users = realm.groups().group(groupId).members().map { UserDTO.UserResponseDTO(it) }
        return ServerResponse.ok().body(users)
    }

    @Transactional
    override fun getUserWithRecipients(id: UUID): ServerResponse {
        val user = UserDTO.UserResponseDTO(realm.users()[id.toString()].toRepresentation())
        val recipients = recipientRepository.findAllByUserId(UUID.fromString(user.id)).map { RecipientDTO.RecipientResponseDTO(it) }
        val response = mapOf(
            "user" to user, "recipients" to recipients
        )
        return ServerResponse.ok().body(response)
    }

    override fun getAllWithRecipients(): ServerResponse {
        val groupId = realm.groups().groups().find { it.name == "User" }?.id ?: return ServerResponse.notFound().build()
        val users = realm.groups().group(groupId).members().map { UserDTO.UserResponseDTO(it) }
        val response = users.map {
            val recipients = recipientRepository.getAllByUserId(UUID.fromString(it.id))
            mapOf(
                "user" to it, "recipients" to recipients
            )
        }
        return ServerResponse.ok().body(response)
    }

    override fun updateUserInformation(id: UUID, data: UserDTO.UpdateUserDTO): ServerResponse {
        val user = realm.users()[id.toString()].toRepresentation()
        user.apply {
            this.firstName = data.firstName
            this.lastName = data.lastName
            this.attributes = this.attributes ?: HashMap()
            this.attributes["patronymic"] = listOf(data.patronymic)
            this.email = data.email
        }
        realm.users()[id.toString()].update(user)
        return ServerResponse.ok().body(UserDTO.UserResponseDTO(user))
    }
}