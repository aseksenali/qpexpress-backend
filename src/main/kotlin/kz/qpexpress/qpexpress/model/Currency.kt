package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity

@Entity
class Currency: AbstractJpaPersistable() {
    lateinit var name: String
}