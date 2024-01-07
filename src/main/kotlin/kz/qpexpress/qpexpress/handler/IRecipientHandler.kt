package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.RecipientDTO
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

fun interface IRecipientHandler {
    fun createRecipient(data: RecipientDTO.CreateRecipientRequestDTO, userId: UUID): ServerResponse
}