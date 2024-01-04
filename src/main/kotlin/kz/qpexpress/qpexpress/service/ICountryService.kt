package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface ICountryService {
    fun createCountry(request: ServerRequest): ServerResponse
    fun getAllCountries(request: ServerRequest): ServerResponse
}