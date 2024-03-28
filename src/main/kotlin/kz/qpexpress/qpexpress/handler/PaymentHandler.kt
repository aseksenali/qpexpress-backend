package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.PaymentStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentHandler(
    private val jetpayHandler: IJetpayHandler,
    private val kaspiHandler: IKaspiHandler,
): IPaymentHandler {
    override fun getPaymentStatus(deliveryId: UUID): Result<PaymentStatus> {
        val jetpayResult = jetpayHandler.getPaymentStatus(deliveryId)
        jetpayResult.onSuccess {
            return Result.success(it)
        }
        val kaspiResult = kaspiHandler.getPaymentStatus(deliveryId)
        kaspiResult.onSuccess {
            return Result.success(it)
        }
        return Result.success(PaymentStatus.NOT_CREATED)
    }
}