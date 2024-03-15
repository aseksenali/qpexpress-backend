package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.model.jetpay.Payment
import org.springframework.web.servlet.function.ServerResponse

interface IJetpayHandler {
    fun createPayment(payment: Payment): ServerResponse
    fun updatePaymentStatus(): ServerResponse
}