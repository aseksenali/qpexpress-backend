package kz.qpexpress.qpexpress.dto

import java.util.*

sealed interface CalculatorDTO {
    data class CalculateDeliveryPriceRequestDTO(
        val countryId: UUID,
        val weight: Float?,
        val price: Float?,
    ) : CalculatorDTO

    data class CalculateDeliveryPriceResponseDTO(
        val countryId: UUID,
        val weight: Float?,
        val price: Float?,
    ) : CalculatorDTO
}