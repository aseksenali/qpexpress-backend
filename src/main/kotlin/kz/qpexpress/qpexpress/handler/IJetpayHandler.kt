package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.JetpayDTO
import kz.qpexpress.qpexpress.dto.PaymentStatus
import java.net.URI
import java.util.*

interface IJetpayHandler {
    fun createPayment(data: JetpayDTO.CreatePaymentRequest): Result<URI>
    fun getPaymentStatus(deliveryId: UUID): Result<PaymentStatus>
    fun updatePaymentStatus(data: JetpayDTO.UpdatePaymentStatusRequest): Result<PaymentStatus>
}