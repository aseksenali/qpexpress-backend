package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Country
import java.util.*

sealed interface CountryDTO {
    data class CreateCountryDTO(
        val nameRus: String,
        val nameKaz: String,
        val nameChn: String,
        val nameEng: String
    ) : CountryDTO {
        fun toEntity(): Country {
            val country = Country()
            country.nameRus = nameRus
            country.nameKaz = nameKaz
            country.nameChn = nameChn
            country.nameEng = nameEng
            return country
        }
    }

    data class CountryResponseDTO(
        val id: UUID,
        val nameRus: String,
        val nameKaz: String,
        val nameChn: String,
        val nameEng: String
    ) : CountryDTO {
        constructor(country: Country) : this(
            id = country.id!!,
            nameRus = country.nameRus,
            nameKaz = country.nameKaz,
            nameChn = country.nameChn,
            nameEng = country.nameEng
        )
    }
}