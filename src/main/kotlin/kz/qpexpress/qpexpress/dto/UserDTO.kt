package kz.qpexpress.qpexpress.dto

import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation

sealed interface UserDTO {
    data class CreateUserDTO(
            val email: String,
            val password: String,
            val group: String,
            val firstName: String,
            val lastName: String,
            val patronymic: String
    ) : UserDTO {
        fun toUserRepresentation(): UserRepresentation {
            return UserRepresentation().also { user ->
                user.email = email
                user.credentials = listOf(
                        CredentialRepresentation().also { credential ->
                            credential.type = CredentialRepresentation.PASSWORD
                            credential.value = password
                            credential.isTemporary = false
                        }
                )
                user.firstName = firstName
                user.lastName = lastName
                user.groups = listOf(group)
                user.isEnabled = true
                user.singleAttribute("patronymic", patronymic)
            }
        }
    }

    data class LoginDTO(
            val email: String,
            val password: String,
    ) : UserDTO

    data class LoginResponseDTO(
            val accessToken: String,
            val refreshToken: String,
    )

    data class UpdateUserDTO(
            val firstName: String,
            val lastName: String,
            val patronymic: String,
            val email: String,
    ) : UserDTO

    data class UserResponseDTO(
            val id: String,
            val firstName: String,
            val lastName: String,
            val patronymic: String,
            val email: String,
    ) : UserDTO {
        constructor(user: UserRepresentation) : this(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            patronymic = user.attributes?.get("patronymic")?.firstOrNull() ?: "",
            email = user.email,
        )
    }
}