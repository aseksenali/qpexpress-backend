package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "deliveries")
class Delivery: AbstractJpaPersistable() {
    var trackingNumber: String? = null
    lateinit var customDeliveryId: String
    lateinit var productLink: String
    lateinit var name: String
    lateinit var description: String
    var price: Float? = null
    var deliveryPrice: Float? = null
    var kazPostTrackNumber: String? = null
    @ManyToOne
    @JoinColumn(name = "currency_id")
    lateinit var currency: Currency
    @ManyToOne
    @JoinColumn(name = "country_id")
    lateinit var country: Country
    @OneToOne
    @JoinColumn(name = "invoice_id")
    var invoice: FileDB? = null
    var weight: Float? = null
    var originalBox: Boolean = false
    lateinit var userId: UUID
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus = DeliveryStatus.CREATED
}