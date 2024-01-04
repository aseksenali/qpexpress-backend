package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class City: AbstractJpaPersistable() {
    lateinit var name: String
    @ManyToOne
    @JoinColumn(name = "country_id")
    lateinit var country: Country
}