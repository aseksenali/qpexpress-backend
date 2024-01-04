package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.CityDTO
import org.springframework.web.servlet.function.ServerResponse

interface ICityHandler {
    fun createCity(data: CityDTO.CreateCityDTO): ServerResponse
    fun getAllCities(): ServerResponse
}