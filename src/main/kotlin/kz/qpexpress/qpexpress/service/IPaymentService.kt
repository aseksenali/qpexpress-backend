package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IPaymentService {
    fun getPaymentStatus(request: ServerRequest): ServerResponse
}