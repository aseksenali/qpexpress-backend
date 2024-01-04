package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
class Recipient: AbstractJpaPersistable() {
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var patronymic: String
    lateinit var country: String
    lateinit var iin: String
    var documentDate: LocalDate? = null
    var documentIssuer: String? = null
    lateinit var userId: UUID
    @OneToOne
    @JoinColumn(name = "document_side_a_id")
    lateinit var documentSideA: FileDB
    @OneToOne
    @JoinColumn(name = "document_side_b_id")
    lateinit var documentSideB: FileDB
    lateinit var district: String
    lateinit var phoneNumber: String
    lateinit var address: String
    @Enumerated(EnumType.STRING)
    lateinit var status: ReceiverStatus
}