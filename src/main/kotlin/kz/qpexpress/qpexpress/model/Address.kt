package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Address: AbstractJpaPersistable() {
    lateinit var country: String
    lateinit var city: String
    lateinit var district: String
    lateinit var neighborhood: String
    lateinit var street: String
    lateinit var house: String
    lateinit var postcode: String
    @Enumerated(EnumType.STRING)
    lateinit var language: Language
}