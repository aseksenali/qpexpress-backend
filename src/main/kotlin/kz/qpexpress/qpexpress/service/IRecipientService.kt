package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

fun interface IRecipientService {
    fun createRecipient(request: ServerRequest): ServerResponse
}