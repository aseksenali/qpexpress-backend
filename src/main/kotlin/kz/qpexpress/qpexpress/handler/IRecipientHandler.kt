package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.RecipientDTO
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IRecipientHandler {
    fun getRecipients(): ServerResponse
    fun createRecipient(data: RecipientDTO.CreateRecipientRequestDTO, userId: UUID): ServerResponse
    fun getMyRecipients(userId: UUID): ServerResponse
    fun updateRecipient(data: RecipientDTO.UpdateRecipientRequestDTO, recipientId: UUID, userId: UUID): ServerResponse
    fun denyRecipient(data: RecipientDTO.DenyRecipientRequestDTO, recipientId: UUID, userId: UUID): ServerResponse
    fun acceptRecipient(recipientId: UUID, userId: UUID): ServerResponse
}