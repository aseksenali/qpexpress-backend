package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface ICityService {
    fun createCity(request: ServerRequest): ServerResponse
    fun getAllCities(request: ServerRequest): ServerResponse
}