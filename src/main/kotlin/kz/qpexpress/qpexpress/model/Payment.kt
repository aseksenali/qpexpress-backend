package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import kz.qpexpress.qpexpress.dto.PaymentStatus
import java.time.LocalDateTime

@Entity
class KaspiPayment(): AbstractJpaPersistable() {
    var paymentId: Long? = null
    var totalAmount: Double? = null
    var availableReturnAmount: Double? = null
    var date: LocalDateTime? = null
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus = PaymentStatus.CREATING

    constructor(paymentId: Long, totalAmount: Double, availableReturnAmount: Double, date: LocalDateTime, status: PaymentStatus): this() {
        this.paymentId = paymentId
        this.totalAmount = totalAmount
        this.availableReturnAmount = availableReturnAmount
        this.date = date
        this.status = status
    }
}