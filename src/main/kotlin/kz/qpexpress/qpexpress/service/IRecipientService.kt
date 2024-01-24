package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IRecipientService {
    fun getRecipients(request: ServerRequest): ServerResponse
    fun createRecipient(request: ServerRequest): ServerResponse
    fun getMyRecipients(request: ServerRequest): ServerResponse
    fun updateRecipient(request: ServerRequest): ServerResponse
    fun denyRecipient(request: ServerRequest): ServerResponse
    fun acceptRecipient(request: ServerRequest): ServerResponse
}