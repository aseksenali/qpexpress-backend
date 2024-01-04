package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CalculatorDTO

fun interface ICalculatorService {
    fun calculateDeliveryPrice(request: CalculatorDTO.CalculateDeliveryPriceRequestDTO): CalculatorDTO.CalculateDeliveryPriceResponseDTO
}