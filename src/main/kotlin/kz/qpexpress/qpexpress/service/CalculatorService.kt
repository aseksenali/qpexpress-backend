package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.CalculatorDTO
import org.springframework.stereotype.Service

@Service
class CalculatorService: ICalculatorService {
    override fun calculateDeliveryPrice(request: CalculatorDTO.CalculateDeliveryPriceRequestDTO): CalculatorDTO.CalculateDeliveryPriceResponseDTO {
        return when {
            request.weight == null && request.price == null -> {
                CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                    countryId = request.countryId,
                    weight = null,
                    price = null,
                )
            }
            request.weight == null && request.price != null -> {
                CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                    countryId = request.countryId,
                    weight = request.price / 13,
                    price = request.price,
                )
            }
            request.price == null && request.weight != null -> {
                CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                    countryId = request.countryId,
                    weight = request.weight,
                    price = request.weight * 13,
                )
            }
            else -> {
                CalculatorDTO.CalculateDeliveryPriceResponseDTO(
                    countryId = request.countryId,
                    weight = request.weight,
                    price = request.price,
                )
            }
        }
    }
}