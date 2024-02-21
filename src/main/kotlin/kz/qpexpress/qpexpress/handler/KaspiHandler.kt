package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.configuration.KaspiProperties
import kz.qpexpress.qpexpress.configuration.PaymentStatusChecker
import kz.qpexpress.qpexpress.dto.KaspiDTO
import kz.qpexpress.qpexpress.dto.PaymentStatus
import kz.qpexpress.qpexpress.dto.TradePoint
import kz.qpexpress.qpexpress.model.KaspiPayment
import kz.qpexpress.qpexpress.repository.KaspiPaymentRepository
import kz.qpexpress.qpexpress.service.KaspiService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDateTime

object ApiEndpoints {
    const val GET_TRADE_POINTS = "/r3/v01/partner/tradepoints/"
    const val REGISTER_DEVICE = "/r3/v01/device/register"
    const val CREATE_PAYMENT = "/r3/v01/remote/create"
    const val CREATE_LINK = "/r3/v01/qr/create-link"
    const val CREATE_QR = "/r3/v01/qr/create"
    const val GET_PAYMENT_METHOD = "/r3/v01/payment/status/"
    const val GET_PAYMENT_DETAILS = "/r3/v01/payment/details"
}

@Service
class KaspiHandler(
    @Qualifier("kaspiRestTemplate")
    private val restTemplate: RestTemplate,
    private val kaspiPaymentRepository: KaspiPaymentRepository,
    private val kaspiProperties: KaspiProperties,
    private val paymentStatusChecker: PaymentStatusChecker
) : IKaspiHandler {
    private final val logger: Logger = LoggerFactory.getLogger(KaspiService::class.java)

    override fun getTradePoints(): KaspiDTO.KaspiResponse<List<TradePoint>> {
        val bin = kaspiProperties.organizationBin
        return restTemplate.getForObject<KaspiDTO.KaspiResponse<List<TradePoint>>>("${ApiEndpoints.GET_TRADE_POINTS}${bin}")
    }

    override fun registerDevice(data: KaspiDTO.RegisterDeviceRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.RegisterDeviceResponse> {
        val requestBody = """
                {
                    "DeviceId": "${data.deviceId}",
                    "TradePointId": ${data.tradePointId},
                    "OrganizationBin": "${kaspiProperties.organizationBin}"
                }
        """.trimIndent()
        return restTemplate.postForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.RegisterDeviceResponse>>(
            ApiEndpoints.REGISTER_DEVICE,
            requestBody
        )
    }

    override fun createPayment(data: KaspiDTO.CreatePaymentRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreatePaymentResponse> {
        val requestBody = """
                {
                    "Amount": ${data.amount},
                    "PhoneNumber": "${data.phoneNumber}",
                    "Comment": "${data.comment}",
                    "DeviceToken": "${data.deviceToken},
                    "OrganizationBin": "${kaspiProperties.organizationBin}"
                }
        """.trimIndent()
        val response = restTemplate.postForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreatePaymentResponse>>(
            ApiEndpoints.CREATE_PAYMENT,
            requestBody
        )
        if (response.statusCode == 0) {
            val payment = KaspiPayment(
                paymentId = response.data.paymentId,
                totalAmount = data.amount,
                availableReturnAmount = 0.0,
                date = LocalDateTime.now(),
                status = PaymentStatus.WAIT,
            )
            kaspiPaymentRepository.save(payment)
        }
        return response
    }

    override fun createLink(data: KaspiDTO.CreateLinkRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateLinkResponse> {
        val payment = KaspiPayment()
        val savedPayment = kaspiPaymentRepository.save(payment)
        val requestBody = """
                {
                    "Amount": ${data.amount},
                    "DeviceToken": "${data.deviceToken}",
                    "OrganizationBin": "${kaspiProperties.organizationBin}",
                    "ExternalId": "${savedPayment.id}"
                }
        """.trimIndent()
        val response = restTemplate.postForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateLinkResponse>>(
            ApiEndpoints.CREATE_LINK,
            requestBody
        )
        if (response.statusCode == 0) {
            paymentStatusChecker.startChecking(response.data.paymentId, response.data.paymentBehaviorOptions.statusPollingInterval * 1000L)
            val detailsResponse = getPaymentDetails(response.data.paymentId, data.deviceToken)
            savedPayment.paymentId = detailsResponse.data.paymentId
            savedPayment.totalAmount = detailsResponse.data.totalAmount
            savedPayment.availableReturnAmount = detailsResponse.data.availableReturnAmount
            savedPayment.date = detailsResponse.data.transactionDate
            savedPayment.status = PaymentStatus.WAIT
            kaspiPaymentRepository.save(savedPayment)
        }
        return response
    }

    override fun createQR(data: KaspiDTO.CreateQRCodeRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateQRCodeResponse> {
        val payment = KaspiPayment()
        val savedPayment = kaspiPaymentRepository.save(payment)
        val requestBody = """
                {
                    "Amount": ${data.amount},
                    "DeviceToken": "${data.deviceToken}",
                    "OrganizationBin": "${kaspiProperties.organizationBin}",
                    "ExternalId": "${savedPayment.id}",
                }
        """.trimIndent()
        val response = restTemplate.postForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateQRCodeResponse>>(
            ApiEndpoints.CREATE_QR,
            requestBody
        )
        if (response.statusCode == 0) {
            paymentStatusChecker.startChecking(response.data.paymentId, response.data.paymentBehaviorOptions.statusPollingInterval * 1000L)
            val detailsResponse = getPaymentDetails(response.data.paymentId, data.deviceToken)
            savedPayment.paymentId = detailsResponse.data.paymentId
            savedPayment.totalAmount = detailsResponse.data.totalAmount
            savedPayment.availableReturnAmount = detailsResponse.data.availableReturnAmount
            savedPayment.date = detailsResponse.data.transactionDate
            savedPayment.status = PaymentStatus.WAIT

            kaspiPaymentRepository.save(savedPayment)
        }
        return response
    }

    override fun getPaymentStatus(paymentId: Int): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentStatusResponse> {
        val response = restTemplate.getForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentStatusResponse>>(
            "${ApiEndpoints.GET_PAYMENT_METHOD}$paymentId"
        )
        if (response.statusCode == 0) {
            val payment = kaspiPaymentRepository.findByPaymentId(paymentId)
            if (payment == null) {
                logger.error("Payment with id $paymentId not found")
                return response
            }
            payment.status = response.data.status
            kaspiPaymentRepository.save(payment)
        }
        return response
    }

    override fun getPaymentDetails(paymentId: Int, deviceToken: String): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentDetailsResponse> {
        val uri = UriComponentsBuilder.fromUriString(ApiEndpoints.GET_PAYMENT_DETAILS)
            .queryParam("QrPaymentId", paymentId)
            .queryParam("DeviceToken", deviceToken)
            .build()
            .toUri()
        val response = restTemplate.getForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentDetailsResponse>>(
            uri
        )
        if (response.statusCode == 0) {
            val payment = kaspiPaymentRepository.findByPaymentId(paymentId)
            if (payment == null) {
                logger.error("Payment with id $paymentId not found")
                return response
            }
            payment.availableReturnAmount = response.data.availableReturnAmount
            payment.date = response.data.transactionDate
            payment.totalAmount = response.data.totalAmount
            kaspiPaymentRepository.save(payment)
        }
        return response
    }
}