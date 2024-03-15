package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.CurrencyDTO
import kz.qpexpress.qpexpress.repository.CurrencyRepository
import kz.qpexpress.qpexpress.service.CurrencyTransferException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.servlet.function.ServerResponse

@Service
class CurrencyHandler(
    private val currencyRepository: CurrencyRepository,
    @Qualifier("currencyRestTemplate") private val restTemplate: RestTemplate
) : ICurrencyHandler {
    override fun createCurrency(data: CurrencyDTO.CreateCurrencyDTO): ServerResponse {
        val savingEntity = data.toEntity()
        val result = currencyRepository.save(savingEntity)
        return ServerResponse.ok().body(CurrencyDTO.CurrencyResponseDTO(result))
    }

    override fun getAllCurrencies(): ServerResponse {
        val result = currencyRepository.findAll().map { CurrencyDTO.CurrencyResponseDTO(it) }
        return ServerResponse.ok().body(result)
    }

    override fun convertCurrency(amount: Float, from: String, to: String): Result<Float> {
        return restTemplate.getForObject<Map<String, Any>>("/${from}.json").let { response ->
            val ratesResponse = response[from] ?: return Result.failure(CurrencyTransferException)
            if (ratesResponse !is Map<*, *>) {
                return Result.failure(CurrencyTransferException)
            }
            val rates = ratesResponse.mapValues { (_, value) -> (value as Number).toFloat() }
            val rate = rates[to] ?: return Result.failure(CurrencyTransferException)
            Result.success(amount * rate)
        }
            .onFailure { Result.failure<CurrencyTransferException>(CurrencyTransferException) }
    }
}