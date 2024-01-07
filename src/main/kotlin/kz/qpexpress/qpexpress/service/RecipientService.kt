package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.RecipientDTO
import kz.qpexpress.qpexpress.handler.IRecipientHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class RecipientService(
    private val recipientHandler: IRecipientHandler
) : IRecipientService {
    override fun createRecipient(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.FORBIDDEN
        ).build()

        val data = request.body<RecipientDTO.CreateRecipientRequestDTO>()
        return recipientHandler.createRecipient(data, userId)
    }
}