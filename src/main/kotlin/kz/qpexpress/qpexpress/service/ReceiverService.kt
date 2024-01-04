package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.ReceiverDTO
import kz.qpexpress.qpexpress.handler.IReceiverHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class ReceiverService(
    private val receiverHandler: IReceiverHandler
) : IReceiverService {
    override fun createReceiver(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.FORBIDDEN
        ).build()
        val data = request.body<ReceiverDTO.CreateReceiverRequestDTO>()
        return receiverHandler.createReceiver(data, userId)
    }
}