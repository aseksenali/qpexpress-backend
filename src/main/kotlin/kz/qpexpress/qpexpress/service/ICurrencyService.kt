package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface ICurrencyService {
    fun createCurrency(request: ServerRequest): ServerResponse
    fun getAllCurrencies(request: ServerRequest): ServerResponse
}