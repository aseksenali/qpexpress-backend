package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "deliveries")
class Delivery: AbstractJpaPersistable() {
    var price: Float? = null
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    lateinit var сurrency: Currency

    var kazPostTrackNumber: String? = null
    var weight: Float? = null
    lateinit var userId: UUID
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus = DeliveryStatus.CREATED

    @OneToMany(mappedBy = "delivery", orphanRemoval = true)
    var goods: MutableSet<Good> = mutableSetOf()
}