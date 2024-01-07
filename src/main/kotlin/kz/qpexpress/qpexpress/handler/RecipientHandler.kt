package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.RecipientDTO
import kz.qpexpress.qpexpress.model.Recipient
import kz.qpexpress.qpexpress.repository.RecipientRepository
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class RecipientHandler(
    private val recipientRepository: RecipientRepository
): IRecipientHandler {
    override fun createRecipient(data: RecipientDTO.CreateRecipientRequestDTO, userId: UUID): ServerResponse {
        val savedEntity = recipientRepository.save(
            Recipient().also {
                it.userId = userId
                it.firstName = data.firstName
                it.lastName = data.lastName
                it.patronymic = data.patronymic
                it.country = data.country
                it.iin = data.iin
                it.documentDate = data.documentDate
                it.documentIssuer = data.documentIssuer
                it.documentSideA = data.documentSideA
                it.documentSideB = data.documentSideB
                it.district = data.district
                it.phoneNumber = data.phoneNumber
                it.address = data.address
            }
        )
        return ServerResponse.ok().body(savedEntity)
    }

}