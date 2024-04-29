package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Address: AbstractJpaPersistable() {
    lateinit var country: String
    var city: String? = null
    var district: String? = null
    var neighborhood: String? = null
    var street: String? = null
    var house: String? = null
    var postcode: String? = null
    @Enumerated(EnumType.STRING)
    lateinit var language: Language
}