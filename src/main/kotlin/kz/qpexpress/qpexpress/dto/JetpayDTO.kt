package kz.qpexpress.qpexpress.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.ZonedDateTime
import java.util.*

sealed interface JetpayDTO {
    data class CreatePaymentRequest(
        val amount: Float,
        val description: String?,
        val deliveryId: UUID,
    ) : JetpayDTO

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class UpdatePaymentStatusRequest(
        val payment: PaymentStatusResponse,
    ) : JetpayDTO

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PaymentStatusResponse(
        val status: String,
        val sum: PaymentSumResponse,
        val date: ZonedDateTime,
        val id: String,
    ) : JetpayDTO

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PaymentSumResponse(
        val amount: Float,
        val currency: String
    ) : JetpayDTO
}