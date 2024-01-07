package kz.qpexpress.qpexpress.dto

import java.util.*

sealed interface DeliveryDTO {
    data class CreateDeliveryRequestDTO(
        val userId: UUID,
        val goods: List<UUID>
    ) : DeliveryDTO

    data class UpdateDeliveryRequestDTO(
        val userId: UUID,
        val weight: Float?,
        val kazPostTrackNumber: String?,
        val price: Float?,
        val currencyId: UUID?,
        val goods: List<UUID>
    ) : DeliveryDTO
}