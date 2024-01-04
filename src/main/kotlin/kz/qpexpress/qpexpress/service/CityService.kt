package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CityDTO
import kz.qpexpress.qpexpress.handler.ICityHandler
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class CityService(
    private val cityHandler: ICityHandler
): ICityService {
    override fun createCity(request: ServerRequest): ServerResponse {
        val data = request.body<CityDTO.CreateCityDTO>()
        return cityHandler.createCity(data)
    }

    override fun getAllCities(request: ServerRequest): ServerResponse {
        return cityHandler.getAllCities()
    }
}