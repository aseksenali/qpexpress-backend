package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.handler.IJetpayHandler
import kz.qpexpress.qpexpress.model.jetpay.Payment
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Service
class JetpayService(
    private val jetpayHandler: IJetpayHandler,
    private val context: ApplicationContext
): IJetpayService {
    override fun getPaymentPageUrl(request: ServerRequest): ServerResponse {
        val payment: Payment = context.getBean(Payment::class.java)
        return jetpayHandler.createPayment(payment)
    }
    override fun updatePaymentStatus(request: ServerRequest): ServerResponse {
        TODO("Not yet implemented")
    }
}