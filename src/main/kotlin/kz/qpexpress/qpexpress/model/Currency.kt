package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity

@Entity
class Currency: AbstractJpaPersistable() {
    lateinit var nameRus: String
    lateinit var nameChn: String
    lateinit var nameEng: String
}