package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.JetpayDTO
import kz.qpexpress.qpexpress.handler.IJetpayHandler
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class JetpayService(
    private val jetpayHandler: IJetpayHandler,
) : IJetpayService {
    override fun getPaymentPageUrl(request: ServerRequest): ServerResponse {
        val payment = request.body<JetpayDTO.CreatePaymentRequest>()
        val result = jetpayHandler.createPayment(payment)
        result.onSuccess {
            return ServerResponse.ok().body(mapOf("uri" to it))
        }.onFailure {
            return ServerResponse.badRequest().body(mapOf("error" to it.message))
        }
        return ServerResponse.badRequest().body(mapOf("error" to "Unknown error"))
    }

    override fun updatePaymentStatus(request: ServerRequest): ServerResponse {
        val payment = request.body<JetpayDTO.UpdatePaymentStatusRequest>()
        val result = jetpayHandler.updatePaymentStatus(payment)
        result.onSuccess {
            return ServerResponse.ok().body(mapOf("status" to it))
        }.onFailure {
            return ServerResponse.badRequest().body(mapOf("error" to it.message))
        }
        return ServerResponse.badRequest().body(mapOf("error" to "Unknown error"))
    }
}