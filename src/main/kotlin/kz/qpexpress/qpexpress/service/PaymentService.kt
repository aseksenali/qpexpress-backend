package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.handler.IPaymentHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Service
class PaymentService(
    private val paymentHandler: IPaymentHandler,
): IPaymentService {
    override fun getPaymentStatus(request: ServerRequest): ServerResponse {
        val deliveryId = request.pathVariable("deliveryId").toUUID() ?: return ServerResponse.badRequest().build()
        val result = paymentHandler.getPaymentStatus(deliveryId)
        result.onSuccess {
            return ServerResponse.ok().body(it)
        }.onFailure {
            return ServerResponse.badRequest().body(mapOf("error" to it.message))
        }
        return ServerResponse.badRequest().body(mapOf("error" to "Unknown error"))
    }
}