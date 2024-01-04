package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.ReceiverDTO
import kz.qpexpress.qpexpress.model.Recipient
import kz.qpexpress.qpexpress.repository.ReceiverRepository
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class ReceiverHandler(
    private val receiverRepository: ReceiverRepository
): IReceiverHandler {
    override fun createReceiver(data: ReceiverDTO.CreateReceiverRequestDTO, userId: UUID): ServerResponse {
        val savedEntity = receiverRepository.save(
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