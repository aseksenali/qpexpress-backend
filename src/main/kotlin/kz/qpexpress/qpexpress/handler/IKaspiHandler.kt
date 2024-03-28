package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.KaspiDTO
import kz.qpexpress.qpexpress.dto.PaymentStatus
import kz.qpexpress.qpexpress.dto.TradePoint
import java.util.UUID

interface IKaspiHandler {
    fun getTradePoints(): Result<KaspiDTO.KaspiResponse<List<TradePoint>>>
    fun registerDevice(data: KaspiDTO.RegisterDeviceRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.RegisterDeviceResponse>>
    fun createPayment(data: KaspiDTO.CreatePaymentRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreatePaymentResponse>>
    fun createLink(data: KaspiDTO.CreateLinkRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateLinkResponse>>
    fun createQR(data: KaspiDTO.CreateQRCodeRequest): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateQRCodeResponse>>
    fun getPaymentStatus(paymentId: Long): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentStatusResponse>>
    fun getPaymentStatus(deliveryId: UUID): Result<PaymentStatus>
    fun getPaymentDetails(paymentId: Long): Result<KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentDetailsResponse>>
}