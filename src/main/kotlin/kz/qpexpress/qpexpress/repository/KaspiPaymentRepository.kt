package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.KaspiPayment
import org.springframework.data.jpa.repository.JpaRepository

interface KaspiPaymentRepository: JpaRepository<KaspiPayment, Int>{
    fun findByPaymentId(paymentId: Long): KaspiPayment?
}