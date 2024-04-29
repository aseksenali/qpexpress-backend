package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.PaymentStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentHandler(
    private val jetpayHandler: IJetpayHandler,
    private val kaspiHandler: IKaspiHandler,
) : IPaymentHandler {
    override fun getPaymentStatus(deliveryId: UUID): Result<PaymentStatus> {
        val jetpayResult = jetpayHandler.getPaymentStatus(deliveryId)
        val jetpayStatus = jetpayResult.getOrNull()
        if (jetpayStatus != null && jetpayStatus != PaymentStatus.WAIT) {
            return Result.success(jetpayStatus)
        }
        val kaspiResult = kaspiHandler.getPaymentStatus(deliveryId)
        val kaspiStatus = kaspiResult.getOrNull()
        if (kaspiStatus != null && kaspiStatus != PaymentStatus.WAIT) {
            return Result.success(kaspiStatus)
        }
        if (jetpayStatus == PaymentStatus.WAIT || kaspiStatus == PaymentStatus.WAIT) {
            return Result.success(PaymentStatus.WAIT)
        }
        return Result.success(PaymentStatus.NOT_CREATED)
    }
}