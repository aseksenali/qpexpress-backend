package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Country
import java.util.*

sealed interface CountryDTO {
    data class CreateCountryDTO(
        val name: String
    ) : CountryDTO {
        fun toEntity(): Country {
            val country = Country()
            country.nameRus = name
            country.nameChn = name
            country.nameEng = name
            return country
        }
    }

    data class CountryResponseDTO(
        val id: UUID,
        val nameRus: String,
        val nameChn: String,
        val nameEng: String
    ) : CountryDTO {
        constructor(country: Country) : this(
            id = country.id!!,
            nameRus = country.nameRus,
            nameChn = country.nameChn,
            nameEng = country.nameEng
        )
    }
}