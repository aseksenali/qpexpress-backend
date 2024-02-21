package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.KaspiDTO
import kz.qpexpress.qpexpress.dto.TradePoint

interface IKaspiHandler {
    fun getTradePoints(): KaspiDTO.KaspiResponse<List<TradePoint>>
    fun registerDevice(data: KaspiDTO.RegisterDeviceRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.RegisterDeviceResponse>
    fun createPayment(data: KaspiDTO.CreatePaymentRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreatePaymentResponse>
    fun createLink(data: KaspiDTO.CreateLinkRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateLinkResponse>
    fun createQR(data: KaspiDTO.CreateQRCodeRequest): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.CreateQRCodeResponse>
    fun getPaymentStatus(paymentId: Int): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentStatusResponse>
    fun getPaymentDetails(paymentId: Int, deviceToken: String): KaspiDTO.KaspiResponse<KaspiDTO.ResponseData.PaymentDetailsResponse>
}