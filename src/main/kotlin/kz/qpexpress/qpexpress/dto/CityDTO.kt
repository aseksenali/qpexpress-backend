package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.City
import kz.qpexpress.qpexpress.model.Country
import java.util.*

sealed interface CityDTO {
    data class CreateCityDTO(
        val nameRus: String,
        val nameChn: String,
        val nameEng: String,
        val countryId: UUID
    ) : CityDTO {
        fun toEntity(country: Country): City {
            val city = City()
            city.nameRus = nameRus
            city.nameChn = nameChn
            city.nameEng = nameEng
            city.country = country
            return city
        }
    }

    data class CityResponseDTO(
        val id: UUID,
        val nameRus: String,
        val nameChn: String,
        val nameEng: String,
        val countryId: UUID,
    ) : CityDTO
}