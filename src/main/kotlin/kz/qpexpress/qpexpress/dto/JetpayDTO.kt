package kz.qpexpress.qpexpress.dto

sealed interface JetpayDTO {
    data class CreatePaymentRequest(
        val orderId: String,
        val amount: Double,
        val currency: String,
        val description: String,
        val successUrl: String,
        val failUrl: String,
        val callbackUrl: String,
    ) : JetpayDTO
}