package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.FileDB
import java.time.LocalDate

sealed interface RecipientDTO {
    data class CreateRecipientRequestDTO(
        val firstName: String,
        val lastName: String,
        val patronymic: String,
        val country: String,
        val iin: String,
        val documentDate: LocalDate,
        val documentIssuer: String,
        val documentSideA: FileDB,
        val documentSideB: FileDB,
        val district: String,
        val phoneNumber: String,
        val address: String,
    ) : RecipientDTO
}