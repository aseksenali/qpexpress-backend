package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
class DeliveryHistory(): AbstractJpaPersistable() {
    lateinit var deliveryId: UUID
    var trackingNumber: String? = null
    var kazPostTrackNumber: String? = null
    lateinit var productLink: String
    lateinit var name: String
    lateinit var description: String
    var price: Float? = null
    var deliveryPrice: Float? = null
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
    @Enumerated(EnumType.STRING)
    lateinit var action: DeliveryAction

    constructor(delivery: Delivery) : this() {
        this.deliveryId = delivery.id!!
        this.trackingNumber = delivery.trackingNumber
        this.kazPostTrackNumber = delivery.kazPostTrackNumber
        this.productLink = delivery.productLink
        this.deliveryPrice = delivery.deliveryPrice
        this.name = delivery.name
        this.description = delivery.description
        this.price = delivery.price
        this.currency = delivery.currency
        this.country = delivery.country
        this.invoice= delivery.invoice
        this.weight = delivery.weight
        this.originalBox = delivery.originalBox
        this.userId = delivery.userId
        this.status = delivery.status
        this.action = DeliveryAction.CREATE
    }
}