package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.City
import kz.qpexpress.qpexpress.model.Country
import java.util.*

sealed interface CityDTO {
    data class CreateCityDTO(
        val name: String,
        val countryId: UUID
    ) : CityDTO {
        fun toEntity(country: Country): City {
            val city = City()
            city.name = name
            city.country = country
            return city
        }
    }

    data class CityResponse(
        val id: UUID,
        val name: String,
        val countryId: UUID,
    ) : CityDTO
}