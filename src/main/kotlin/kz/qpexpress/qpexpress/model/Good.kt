package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "goods")
class Good: AbstractJpaPersistable() {
    lateinit var name: String
    lateinit var description: String
    @ManyToOne
    @JoinColumn(name = "country_id")
    lateinit var country: Country
    lateinit var customOrderId: String
    lateinit var link: String
    @Column(name = "price", nullable = false)
    var price: Float? = null
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
    @Enumerated(EnumType.STRING)
    lateinit var status: GoodStatus

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Good

        if (name != other.name) return false
        if (description != other.description) return false
        if (country != other.country) return false
        if (customOrderId != other.customOrderId) return false
        if (link != other.link) return false
        if (price != other.price) return false
        if (currency != other.currency) return false
        if (invoice != other.invoice) return false
        if (trackingNumber != other.trackingNumber) return false
        if (originalBox != other.originalBox) return false
        if (order != other.order) return false
        if (delivery != other.delivery) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + customOrderId.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + currency.hashCode()
        result = 31 * result + (invoice?.hashCode() ?: 0)
        result = 31 * result + (trackingNumber?.hashCode() ?: 0)
        result = 31 * result + originalBox.hashCode()
        result = 31 * result + order.hashCode()
        result = 31 * result + (delivery?.hashCode() ?: 0)
        return result
    }
}