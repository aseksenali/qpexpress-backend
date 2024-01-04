package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CountryDTO
import kz.qpexpress.qpexpress.handler.ICountryHandler
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class CountryService(
    private val countryHandler: ICountryHandler
): ICountryService {
    override fun createCountry(request: ServerRequest): ServerResponse {
        val data = request.body<CountryDTO.CreateCountryDTO>()
        return countryHandler.createCountry(data)
    }

    override fun getAllCountries(request: ServerRequest): ServerResponse {
        return countryHandler.getAllCountries()
    }
}