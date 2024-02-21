package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CalculatorDTO
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.time.LocalDate

data class CurrencyResponse(
    val date: LocalDate,
    val kzt: Float,
)

@Service
class CalculatorService(
    restTemplateBuilder: RestTemplateBuilder
) : ICalculatorService {
    val restTemplate: RestTemplate = restTemplateBuilder.rootUri("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies").build()
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
                priceKZT = convertToKZT(request.weight * 13),
            )
        }
    }

    private fun convertToKZT(priceUSD: Float): Float {
        restTemplate.getForObject<CurrencyResponse>("/usd/kzt.json").let {
            return priceUSD * it.kzt
        }
    }
}