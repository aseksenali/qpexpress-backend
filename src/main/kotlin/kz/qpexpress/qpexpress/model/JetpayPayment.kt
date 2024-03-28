package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import kz.qpexpress.qpexpress.dto.PaymentStatus
import java.time.LocalDateTime

@Entity
class JetpayPayment(): AbstractJpaPersistable() {
    @Column(unique = true, nullable = false)
    var paymentId: Long = 0
    var totalAmount: Float? = null
    var date: LocalDateTime? = null
    @ManyToOne
    @JoinColumn(name = "delivery_id", unique = false)
    lateinit var delivery: Delivery
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus = PaymentStatus.CREATING
}