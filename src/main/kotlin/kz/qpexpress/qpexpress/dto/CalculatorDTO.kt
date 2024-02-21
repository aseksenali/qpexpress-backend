package kz.qpexpress.qpexpress.dto

import java.util.*

sealed interface CalculatorDTO {
    data class CalculateDeliveryPriceRequestDTO(
        val countryId: UUID,
        val weight: Float?,
    ) : CalculatorDTO

    data class CalculateDeliveryPriceResponseDTO(
        val countryId: UUID,
        val weight: Float?,
        val priceUSD: Float?,
        val priceKZT: Float?,
    ) : CalculatorDTO
}