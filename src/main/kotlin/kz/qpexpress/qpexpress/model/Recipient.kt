package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.util.*

@Entity
class Recipient: AbstractJpaPersistable() {
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var patronymic: String
    lateinit var iin: String
    lateinit var userId: UUID
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_side_a_id")
    lateinit var documentSideA: FileDB
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_side_b_id")
    lateinit var documentSideB: FileDB
    lateinit var district: String
    lateinit var phoneNumber: String
    lateinit var address: String
    @Enumerated(EnumType.STRING)
    lateinit var status: RecipientStatus
    var comment: String? = null
    @OneToMany(mappedBy = "recipient")
    var orders: MutableSet<Order> = mutableSetOf()
}