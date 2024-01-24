package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.model.Recipient
import kz.qpexpress.qpexpress.model.RecipientStatus
import java.time.LocalDateTime
import java.util.*

sealed interface RecipientDTO {
    data class CreateRecipientRequestDTO(
        val firstName: String,
        val lastName: String,
        val patronymic: String,
        val iin: String,
        val documentSideA: FileDB,
        val documentSideB: FileDB,
        val district: String,
        val phoneNumber: String,
        val address: String,
    ) : RecipientDTO

    data class UpdateRecipientRequestDTO(
        val firstName: String,
        val lastName: String,
        val patronymic: String,
        val iin: String,
        val documentSideA: FileDB,
        val documentSideB: FileDB,
        val district: String,
        val phoneNumber: String,
        val address: String,
        val comment: String?,
    ) : RecipientDTO

    data class RecipientResponseDTO(
        val id: UUID,
        val firstName: String,
        val lastName: String,
        val patronymic: String,
        val iin: String,
        val documentSideA: FileDTO.FileResponseDTO,
        val documentSideB: FileDTO.FileResponseDTO,
        val district: String,
        val phoneNumber: String,
        val address: String,
        val status: RecipientStatus,
        val comment: String?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
    ) : RecipientDTO {
        constructor(recipient: Recipient) : this(
            id = recipient.id!!,
            firstName = recipient.firstName,
            lastName = recipient.lastName,
            patronymic = recipient.patronymic,
            iin = recipient.iin,
            documentSideA = FileDTO.FileResponseDTO(recipient.documentSideA),
            documentSideB = FileDTO.FileResponseDTO(recipient.documentSideB),
            district = recipient.district,
            phoneNumber = recipient.phoneNumber,
            address = recipient.address,
            status = recipient.status,
            comment = recipient.comment,
            createdAt = recipient.createdAt,
            updatedAt = recipient.updatedAt,
        )
    }

    data class DenyRecipientRequestDTO(
        val comment: String,
    ) : RecipientDTO
}