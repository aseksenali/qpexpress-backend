package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "orders")
class Order: AbstractJpaPersistable() {
    lateinit var userId: UUID
    lateinit var orderNumber: String
    @Enumerated(EnumType.STRING)
    lateinit var status: OrderStatus
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var goods: MutableSet<Good> = mutableSetOf()

    @ManyToOne(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH],
        optional = false,
    )
    @JoinColumn(name = "recipient_id", nullable = false)
    lateinit var recipient: Recipient

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        if (userId != other.userId) return false
        if (orderNumber != other.orderNumber) return false
        if (status != other.status) return false
        if (goods != other.goods) return false
        if (recipient != other.recipient) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + orderNumber.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + goods.hashCode()
        result = 31 * result + (recipient.hashCode())
        return result
    }
}