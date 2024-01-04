package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.CountryDTO
import org.springframework.web.servlet.function.ServerResponse

interface ICountryHandler {
    fun createCountry(data: CountryDTO.CreateCountryDTO): ServerResponse
    fun getAllCountries(): ServerResponse
}