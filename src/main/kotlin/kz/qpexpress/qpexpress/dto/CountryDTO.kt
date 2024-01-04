package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Country
import java.util.*

sealed interface CountryDTO {
    data class CreateCountryDTO(
        val name: String
    ) : CountryDTO {
        fun toEntity(): Country {
            val country = Country()
            country.name = name
            return country
        }
    }

    data class CountryResponse(
        val id: UUID,
        val name: String,
    ) : CountryDTO
}