package kz.qpexpress.qpexpress.dto

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

data class TradePoint(
    @JsonAlias("TradePointId")
    val tradePointId: Int,
    @JsonAlias("TradePointName")
    val tradePointName: String,
)

sealed interface KaspiDTO {
    data class RegisterDeviceRequest(
        val deviceId: String,
        val tradePointId: Int
    ) : KaspiDTO

    data class CreatePaymentRequest(
        val phoneNumber: String,
        val amount: Double,
        val comment: String,
        val deviceToken: String,
    ) : KaspiDTO

    data class CreateLinkRequest(
        val amount: Double,
        val deviceToken: String,
    ) : KaspiDTO

    data class CreateQRCodeRequest(
        val amount: Double,
        val deviceToken: String,
    ) : KaspiDTO

    data class KaspiResponse<T>(
        @JsonAlias("StatusCode")
        val statusCode: Int,
        @JsonAlias("Data")
        val data: T
    ) : KaspiDTO

    sealed interface ResponseData {
        data class RegisterDeviceResponse(
            @JsonAlias("DeviceToken")
            val deviceToken: String,
        ) : ResponseData

        data class CreatePaymentResponse(
            @JsonAlias("QrPaymentId")
            val paymentId: Int,
        ) : ResponseData

        data class PaymentBehaviorOptions(
            @JsonAlias("LinkActivationWaitTimeout")
            val linkActivationWaitTimeout: Int,
            @JsonAlias("PaymentConfirmationTimeout")
            val paymentConfirmationTimeout: Int,
            @JsonAlias("StatusPollingInterval")
            val statusPollingInterval: Int,
        )

        data class CreateLinkResponse(
            @JsonAlias("PaymentLink")
            val paymentLink: String,
            @JsonAlias("ExpireDate")
            val expireDate: LocalDateTime,
            @JsonAlias("PaymentId")
            val paymentId: Int,
            @JsonAlias("PaymentMethods")
            val paymentMethods: List<String>,
            @JsonAlias("PaymentBehaviorOptions")
            val paymentBehaviorOptions: PaymentBehaviorOptions,
        ) : ResponseData

        data class CreateQRCodeResponse(
            @JsonAlias("QrToken")
            val token: String,
            @JsonAlias("ExpireDate")
            val expireDate: LocalDateTime,
            @JsonAlias("QrPaymentId")
            val paymentId: Int,
            @JsonAlias("PaymentMethods")
            val paymentMethods: List<String>,
            @JsonAlias("QrPaymentBehaviorOptions")
            val paymentBehaviorOptions: PaymentBehaviorOptions,
        )

        data class PaymentStatusResponse(
            @JsonAlias("Status")
            @Enumerated(EnumType.STRING)
            val status: PaymentStatus,
        )

        data class PaymentDetailsResponse(
            @JsonAlias("QrPaymentId")
            val paymentId: Int,
            @JsonAlias("TotalAmount")
            val totalAmount: Double,
            @JsonAlias("AvailableReturnAmount")
            val availableReturnAmount: Double,
            @JsonAlias("TransactionDate")
            val transactionDate: LocalDateTime,
        )
    }
}