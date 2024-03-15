package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.model.jetpay.Gate
import kz.qpexpress.qpexpress.model.jetpay.Payment
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI

@Service
class JetpayHandler(
    private val gate: Gate
): IJetpayHandler {
    override fun createPayment(payment: Payment): ServerResponse {
        val uri = URI.create(gate.getPurchasePaymentPageUrl(payment))
        return ServerResponse.permanentRedirect(uri).build()
    }

    override fun updatePaymentStatus(): ServerResponse {
        TODO("Not yet implemented")
    }
}