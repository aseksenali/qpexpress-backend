package kz.qpexpress.qpexpress.dto

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime
import java.time.OffsetDateTime

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
    ) : KaspiDTO

    data class CreateLinkRequest(
        val amount: Double,
    ) : KaspiDTO

    data class CreateQRCodeRequest(
        val amount: Double,
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
            val paymentId: Long,
        ) : ResponseData

        data class LinkPaymentBehaviorOptions(
            @JsonAlias("LinkActivationWaitTimeout")
            val linkActivationWaitTimeout: Long,
            @JsonAlias("PaymentConfirmationTimeout")
            val paymentConfirmationTimeout: Long,
            @JsonAlias("StatusPollingInterval")
            val statusPollingInterval: Long,
        )

        data class QrPaymentBehaviorOptions(
            @JsonAlias("QrCodeScanWaitTimeout")
            val qrCodeScanWaitTimeout: Long,
            @JsonAlias("PaymentConfirmationTimeout")
            val paymentConfirmationTimeout: Long,
            @JsonAlias("StatusPollingInterval")
            val statusPollingInterval: Long,
        )

        data class CreateLinkResponse(
            @JsonAlias("PaymentLink")
            val paymentLink: String,
            @JsonAlias("ExpireDate")
            val expireDate: OffsetDateTime,
            @JsonAlias("PaymentId")
            val paymentId: Long,
            @JsonAlias("PaymentMethods")
            val paymentMethods: List<String>,
            @JsonAlias("PaymentBehaviorOptions")
            val paymentBehaviorOptions: LinkPaymentBehaviorOptions,
        ) : ResponseData

        data class CreateQRCodeResponse(
            @JsonAlias("QrToken")
            val token: String,
            @JsonAlias("ExpireDate")
            val expireDate: OffsetDateTime,
            @JsonAlias("QrPaymentId")
            val paymentId: Long,
            @JsonAlias("PaymentMethods")
            val paymentMethods: List<String>,
            @JsonAlias("QrPaymentBehaviorOptions")
            val paymentBehaviorOptions: QrPaymentBehaviorOptions,
        )

        data class PaymentStatusResponse(
            @JsonAlias("Status")
            @Enumerated(EnumType.STRING)
            val status: PaymentStatus,
        )

        data class PaymentDetailsResponse(
            @JsonAlias("QrPaymentId")
            val paymentId: Long,
            @JsonAlias("TotalAmount")
            val totalAmount: Double,
            @JsonAlias("AvailableReturnAmount")
            val availableReturnAmount: Double,
            @JsonAlias("TransactionDate")
            val transactionDate: LocalDateTime,
        )
    }
}