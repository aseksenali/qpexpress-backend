package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CurrencyDTO
import kz.qpexpress.qpexpress.handler.ICurrencyHandler
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class CurrencyService(
    private val currencyHandler: ICurrencyHandler
) : ICurrencyService {
    override fun createCurrency(request: ServerRequest): ServerResponse {
        val data = request.body<CurrencyDTO.CreateCurrencyDTO>()
        return currencyHandler.createCurrency(data)
    }

    override fun getAllCurrencies(request: ServerRequest): ServerResponse {
        return currencyHandler.getAllCurrencies()
    }
}