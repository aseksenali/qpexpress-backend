package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.PaymentStatus
import java.util.UUID

interface IPaymentHandler {
    fun getPaymentStatus(deliveryId: UUID): Result<PaymentStatus>
}