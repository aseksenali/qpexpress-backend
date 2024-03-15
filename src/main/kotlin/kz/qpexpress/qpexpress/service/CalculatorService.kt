package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CalculatorDTO
import kz.qpexpress.qpexpress.handler.CurrencyHandler
import org.springframework.stereotype.Service

@Service
class CalculatorService(
    private val currencyHandler: CurrencyHandler,
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
            CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                countryId = request.countryId,
                weight = request.weight,
                priceUSD = request.weight * 13,
                priceKZT = currencyHandler.convertCurrency(request.weight * 13, "usd", "kzt").getOrNull(),
            )
        }
    }
}