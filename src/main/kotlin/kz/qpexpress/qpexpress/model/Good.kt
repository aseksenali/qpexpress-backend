package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "goods")
class Good: AbstractJpaPersistable() {
    lateinit var name: String
    lateinit var description: String
    lateinit var customOrderId: String
    @ManyToOne
    @JoinColumn(name = "country_id")
    lateinit var country: Country
    lateinit var link: String
    @Column(name = "price", nullable = false)
    var price: Float = 0.0f
    @ManyToOne
    @JoinColumn(name = "currency_id")
    lateinit var currency: Currency
    @OneToOne
    @JoinColumn(name = "invoice_id")
    var invoice: FileDB? = null
    var trackingNumber: String? = null
    var originalBox: Boolean = false
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    lateinit var order: Order
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = true)
    var delivery: Delivery? = null
    var quantity: Int = 1
    lateinit var userId: UUID
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    lateinit var recipient: Recipient
    @Enumerated(EnumType.STRING)
    lateinit var status: GoodStatus
}