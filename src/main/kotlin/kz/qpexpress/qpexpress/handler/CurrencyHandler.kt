package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.CurrencyDTO
import kz.qpexpress.qpexpress.repository.CurrencyRepository
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse

@Service
class CurrencyHandler(
    private val currencyRepository: CurrencyRepository
): ICurrencyHandler {
    override fun createCurrency(data: CurrencyDTO.CreateCurrencyDTO): ServerResponse {
        val savingEntity = data.toEntity()
        val result = currencyRepository.save(savingEntity)
        return ServerResponse.ok().body(CurrencyDTO.CurrencyResponse(
            id = result.id!!,
            name = result.name,
        ))
    }

    override fun getAllCurrencies(): ServerResponse {
        val result = currencyRepository.findAll()
        return ServerResponse.ok().body(result)
    }
}