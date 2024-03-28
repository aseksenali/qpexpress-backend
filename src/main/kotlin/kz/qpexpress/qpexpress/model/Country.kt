package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
class Country: AbstractJpaPersistable() {
    lateinit var nameRus: String
    lateinit var nameChn: String
    lateinit var nameEng: String
    @OneToMany(mappedBy = "country")
    lateinit var cities: Set<City>
}