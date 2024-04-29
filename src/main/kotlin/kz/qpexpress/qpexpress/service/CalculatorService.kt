package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CalculatorDTO
import kz.qpexpress.qpexpress.handler.CurrencyHandler
import kz.qpexpress.qpexpress.repository.CountryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CalculatorService(
    private val currencyHandler: CurrencyHandler,
    private val countryRepository: CountryRepository,
) : ICalculatorService {
    override fun calculateDeliveryPrice(request: CalculatorDTO.CalculateDeliveryPriceRequestDTO): CalculatorDTO.CalculateDeliveryPriceResponseDTO {
        return if (request.weight == null) {
            CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                countryId = request.countryId,
                weight = null,
                priceUSD = null,
                priceKZT = null,
            )
        } else {
            val country = countryRepository.findByIdOrNull(request.countryId)
                ?: throw IllegalArgumentException("Country with id ${request.countryId} not found")
            return when (country.nameRus) {
                "Турция" ->
                    CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                        countryId = request.countryId,
                        weight = request.weight,
                        priceUSD = request.weight * 13,
                        priceKZT = currencyHandler.convertCurrency(request.weight * 13, "usd", "kzt").getOrNull(),
                    )
                "Южная Корея" ->
                    CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                        countryId = request.countryId,
                        weight = request.weight,
                        priceUSD = request.weight * 12,
                        priceKZT = currencyHandler.convertCurrency(request.weight * 12, "usd", "kzt").getOrNull(),
                    )
                "Германия" ->
                    CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                        countryId = request.countryId,
                        weight = request.weight,
                        priceUSD = request.weight * 11,
                        priceKZT = currencyHandler.convertCurrency(request.weight * 11, "usd", "kzt").getOrNull(),
                    )
                else ->
                    throw IllegalArgumentException("Country ${country.nameRus} is not supported")
            }
        }
    }
}