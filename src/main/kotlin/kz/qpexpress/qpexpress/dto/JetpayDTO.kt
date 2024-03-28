package kz.qpexpress.qpexpress.dto

import java.time.LocalDateTime
import java.util.UUID

sealed interface JetpayDTO {
    data class CreatePaymentRequest(
        val amount: Float,
        val description: String?,
        val deliveryId: UUID,
    ) : JetpayDTO

    data class UpdatePaymentStatusRequest(
        val payment: PaymentStatusResponse,
    ) : JetpayDTO

    data class PaymentStatusResponse(
        val status: String,
        val sum: PaymentSumResponse,
        val date: LocalDateTime
    ) : JetpayDTO

    data class PaymentSumResponse(
        val amount: Float,
        val currency: String
    ) : JetpayDTO
}