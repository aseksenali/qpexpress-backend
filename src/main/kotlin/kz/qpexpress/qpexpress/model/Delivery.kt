package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "deliveries")
class Delivery: AbstractJpaPersistable() {
    var price: Float = 0.0f
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    lateinit var currency: Currency
    @Column(name = "delivery_number", unique = true)
    lateinit var deliveryNumber: String
    var kazPostTrackNumber: String? = null
    @OneToOne
    @JoinColumn(name = "invoice_id")
    var invoice: FileDB? = null
    var weight: Float = 0.0f
    lateinit var userId: UUID
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    lateinit var recipient: Recipient
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus = DeliveryStatus.CREATED
    @OneToMany(mappedBy = "delivery")
    var goods: MutableSet<Good> = mutableSetOf()
    var payed: Boolean = false
}