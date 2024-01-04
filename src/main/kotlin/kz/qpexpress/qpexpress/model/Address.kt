package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity

@Entity
class Address: AbstractJpaPersistable() {
    lateinit var country: String
    lateinit var city: String
    lateinit var district: String
    lateinit var neighborhood: String
    lateinit var street: String
    lateinit var house: String
    lateinit var postcode: String
}