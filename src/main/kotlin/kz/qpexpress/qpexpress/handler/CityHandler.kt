package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.CityDTO
import kz.qpexpress.qpexpress.repository.CityRepository
import kz.qpexpress.qpexpress.repository.CountryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse

@Service
class CityHandler(
    private val countryRepository: CountryRepository,
    private val cityRepository: CityRepository,
) : ICityHandler {
    override fun createCity(data: CityDTO.CreateCityDTO): ServerResponse {
        val country = countryRepository.findByIdOrNull(data.countryId) ?: return ServerResponse.badRequest().build()
        val savingEntity = data.toEntity(country)
        val result = cityRepository.save(savingEntity)
        return ServerResponse.ok().body(CityDTO.CityResponse(
            id = result.id!!,
            name = result.name,
            countryId = country.id!!,
        ))
    }

    override fun getAllCities(): ServerResponse {
        val result = cityRepository.findAll()
        return ServerResponse.ok().body(result)
    }
}