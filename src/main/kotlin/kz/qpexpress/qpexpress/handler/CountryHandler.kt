package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.CountryDTO
import kz.qpexpress.qpexpress.repository.CountryRepository
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse

@Service
class CountryHandler(
    private val countryRepository: CountryRepository,
): ICountryHandler {
    override fun createCountry(data: CountryDTO.CreateCountryDTO): ServerResponse {
        val savingEntity = data.toEntity()
        val result = countryRepository.save(savingEntity)
        return ServerResponse.ok().body(CountryDTO.CountryResponse(
            id = result.id!!,
            name = result.name,
        ))
    }

    override fun getAllCountries(): ServerResponse {
        val result = countryRepository.findAll()
        return ServerResponse.ok().body(result)
    }
}