package kz.qpexpress.qpexpress.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kz.qpexpress.qpexpress.configuration.KaspiProperties
import kz.qpexpress.qpexpress.configuration.PaymentStatusChecker
import kz.qpexpress.qpexpress.dto.KaspiDTO
import kz.qpexpress.qpexpress.dto.PaymentStatus
import kz.qpexpress.qpexpress.dto.TradePoint
import kz.qpexpress.qpexpress.exception.NotFoundException
import kz.qpexpress.qpexpress.model.KaspiPayment
import kz.qpexpress.qpexpress.repository.DeliveryRepository
import kz.qpexpress.qpexpress.repository.KaspiPaymentRepository
import kz.qpexpress.qpexpress.service.KaspiService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
import java.time.LocalDateTime
import java.util.*

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
    private val deliveryRepository: DeliveryRepository,
    private val paymentStatusChecker: PaymentStatusChecker,
    private val mapper: ObjectMapper
) : IKaspiHandler {
    private final val logger: Logger = LoggerFactory.getLogger(KaspiService::class.java)

    override fun getTradePoints(): Result<KaspiDTO.KaspiResponse<List<TradePoint>>> {
        val bin = kaspiProperties.organizationBin
        val response = restTemplate.getForObject<KaspiDTO.KaspiResponse<List<TradePoint>>>("${ApiEndpoints.GET_TRADE_POINTS}${bin}")
        return Result.success(response)
    }

    override fun registerDevice(data: KaspiDTO.RegisterDeviceRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.RegisterDeviceResponse>> {
        val requestBody = """
                {
                    "DeviceId": "${data.deviceId}",
                    "TradePointId": ${data.tradePointId},
                    "OrganizationBin": "${kaspiProperties.organizationBin}"
                }
        """.trimIndent()
        val response = restTemplate.postForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.RegisterDeviceResponse>>(
            ApiEndpoints.REGISTER_DEVICE,
            requestBody
        )

        return Result.success(response)
    }

    override fun createPayment(data: KaspiDTO.CreatePaymentRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreatePaymentResponse>> {
        val requestBody = """
                {
                    "Amount": ${data.amount},
                    "PhoneNumber": "${data.phoneNumber}",
                    "Comment": "${data.comment}",
                    "DeviceToken": "${kaspiProperties.deviceToken},
                    "OrganizationBin": "${kaspiProperties.organizationBin}"
                }
        """.trimIndent()
        val response = restTemplate.postForObject<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreatePaymentResponse>>(
            ApiEndpoints.CREATE_PAYMENT,
            requestBody
        )
        if (response.statusCode == 0 && response.data != null) {
            val payment = KaspiPayment(
                paymentId = response.data.paymentId,
                phoneNumber = data.phoneNumber,
                totalAmount = data.amount,
                availableReturnAmount = 0.0,
                date = LocalDateTime.now(),
                status = PaymentStatus.WAIT,
            )
            kaspiPaymentRepository.save(payment)
        }
        return Result.success(response)
    }

    override fun createLink(data: KaspiDTO.CreateLinkRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateLinkResponse>> {
        val delivery = deliveryRepository.findByIdOrNull(data.deliveryId) ?: return Result.failure(NotFoundException)
        val payment = KaspiPayment().also {
            it.delivery = delivery
            it.totalAmount = data.amount
        }
        val savedPayment = kaspiPaymentRepository.save(payment)
        val requestBody = """
                {
                    "Amount": ${data.amount},
                    "DeviceToken": "${kaspiProperties.deviceToken}",
                    "OrganizationBin": "${kaspiProperties.organizationBin}",
                    "ExternalId": "${savedPayment.id}"
                }
        """.trimIndent()
        val response = restTemplate.postForObject<String>(
            ApiEndpoints.CREATE_LINK,
            requestBody
        )
        val result = mapper.readValue<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateLinkResponse>>(response)
        if (result.statusCode == 0 && result.data != null) {
            logger.info("Polling interval: ${result.data.paymentBehaviorOptions.statusPollingInterval} seconds")
            paymentStatusChecker.startChecking(result.data.paymentId, result.data.paymentBehaviorOptions.statusPollingInterval)
            savedPayment.paymentId = result.data.paymentId
            kaspiPaymentRepository.save(savedPayment)
        }
        return Result.success(result)
    }

    override fun createQR(data: KaspiDTO.CreateQRCodeRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateQRCodeResponse>> {
        val delivery = deliveryRepository.findByIdOrNull(data.deliveryId) ?: return Result.failure(NotFoundException)
        val payment = KaspiPayment().also {
            it.delivery = delivery
            it.totalAmount = data.amount
        }
        val savedPayment = kaspiPaymentRepository.save(payment)
        val requestBody = """
                {
                    "Amount": ${data.amount},
                    "DeviceToken": "${kaspiProperties.deviceToken}",
                    "OrganizationBin": "${kaspiProperties.organizationBin}",
                    "ExternalId": "${savedPayment.id}",
                }
        """.trimIndent()
        val response = restTemplate.postForObject<String>(
            ApiEndpoints.CREATE_QR,
            requestBody
        )
        logger.info("Response: $response")
        val result = mapper.readValue<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateQRCodeResponse>>(response)
        if (result.statusCode == 0 && result.data != null) {
            logger.info("Polling interval: ${result.data.paymentBehaviorOptions.statusPollingInterval} seconds")
            paymentStatusChecker.startChecking(result.data.paymentId, result.data.paymentBehaviorOptions.statusPollingInterval)
            savedPayment.paymentId = result.data.paymentId
            kaspiPaymentRepository.save(savedPayment)
        }
        return Result.success(result)
    }

    override fun getPaymentStatus(paymentId: Long): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentStatusResponse>> {
        val response = restTemplate.getForObject<String>(
            "${ApiEndpoints.GET_PAYMENT_METHOD}$paymentId"
        )
        logger.info("Response: $response")
        val result = mapper.readValue<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentStatusResponse>>(response)
        if (result.statusCode == 0 && result.data != null) {
            val payment = kaspiPaymentRepository.findByPaymentId(paymentId)
            if (payment == null) {
                logger.error("Payment with id $paymentId not found")
                return Result.success(result)
            }
            payment.status = result.data.status
            kaspiPaymentRepository.save(payment)
        }
        return Result.success(result)
    }

    override fun getPaymentStatus(deliveryId: UUID): Result<PaymentStatus> {
        val payment = kaspiPaymentRepository.findByDeliveryId(deliveryId)
        if (payment.isEmpty()) {
            return Result.failure(NotFoundException)
        }
        if (payment.any { it.status == PaymentStatus.PROCESSED }) {
            return Result.success(PaymentStatus.PROCESSED)
        }
        if (payment.all { it.status == PaymentStatus.ERROR }) {
            return Result.success(PaymentStatus.ERROR)
        }
        return Result.success(PaymentStatus.WAIT)
    }

    override fun getPaymentDetails(paymentId: Long): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentDetailsResponse>> {
        val deviceToken = kaspiProperties.deviceToken
        val url = "${ApiEndpoints.GET_PAYMENT_DETAILS}?QrPaymentId=$paymentId&DeviceToken=$deviceToken"
        val response = restTemplate.getForObject<String>(
            url
        )
        logger.info("Response: $response")
        val result = mapper.readValue<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentDetailsResponse>>(response)
        if (result.statusCode == 0 && result.data != null) {
            val payment = kaspiPaymentRepository.findByPaymentId(paymentId)
            if (payment == null) {
                logger.error("Payment with id $paymentId not found")
                return Result.success(result)
            }
            payment.availableReturnAmount = result.data.availableReturnAmount
            payment.date = result.data.transactionDate
            payment.totalAmount = result.data.totalAmount
            kaspiPaymentRepository.save(payment)
        }
        return Result.success(result)
    }
}