package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.KaspiDTO
import kz.qpexpress.qpexpress.handler.IJetpayHandler
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class JetpayService(
    private val jetpayHandler: IJetpayHandler
): IJetpayService {
    override fun createPayment(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.CreatePaymentRequest>()
        return jetpayHandler.createPayment(data)
    }

    override fun updatePaymentStatus(request: ServerRequest): ServerResponse {
        TODO("Not yet implemented")
    }
}