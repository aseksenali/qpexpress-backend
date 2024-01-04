package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.CurrencyDTO
import org.springframework.web.servlet.function.ServerResponse

interface ICurrencyHandler {
    fun createCurrency(data: CurrencyDTO.CreateCurrencyDTO): ServerResponse
    fun getAllCurrencies(): ServerResponse
}