package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IJetpayService {
    fun createPayment(request: ServerRequest): ServerResponse
    fun updatePaymentStatus(request: ServerRequest): ServerResponse
}