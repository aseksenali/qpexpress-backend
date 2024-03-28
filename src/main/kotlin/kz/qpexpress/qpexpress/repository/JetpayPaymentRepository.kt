package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.JetpayPayment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JetpayPaymentRepository: JpaRepository<JetpayPayment, Int> {
    fun findByPaymentId(paymentId: Long): JetpayPayment?
    fun findAllByDeliveryId(deliveryId: UUID): List<JetpayPayment>
    fun findFirstByOrderByPaymentIdDesc(): JetpayPayment?
}