package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CurrencyDTO
import kz.qpexpress.qpexpress.handler.ICurrencyHandler
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull
import java.time.LocalDate

data class CurrencyResponse(
    val date: LocalDate,

)

object CurrencyTransferException: Exception("Failed transferring currency") {
    private fun readResolve(): Any = CurrencyTransferException
}

@Service
class CurrencyService(
    private val currencyHandler: ICurrencyHandler,
) : ICurrencyService {
    override fun createCurrency(request: ServerRequest): ServerResponse {
        val data = request.body<CurrencyDTO.CreateCurrencyDTO>()
        return currencyHandler.createCurrency(data)
    }

    override fun getAllCurrencies(request: ServerRequest): ServerResponse {
        return currencyHandler.getAllCurrencies()
    }

    override fun convertCurrency(request: ServerRequest): ServerResponse {
        val amount = request.paramOrNull("amount")?.toFloatOrNull() ?: return ServerResponse.badRequest().build()
        val from = request.paramOrNull("from") ?: return ServerResponse.badRequest().build()
        val to = request.paramOrNull("to") ?: return ServerResponse.badRequest().build()
        return currencyHandler.convertCurrency(amount, from, to).fold(
            onSuccess = { ServerResponse.ok().body(it) },
            onFailure = { ServerResponse.badRequest().build() }
        )
    }
}