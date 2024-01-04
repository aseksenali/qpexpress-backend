package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.DeliveryStatus
import kz.qpexpress.qpexpress.model.FileDB
import java.time.LocalDateTime
import java.util.*

sealed interface DeliveryDTO {
    data class CreateDeliveryRequestDTO(
        val userId: UUID,
        val orderId: String,
        val trackingNumber: String?,
        val productLink: String,
        val name: String,
        val description: String,
        val price: Float?,
        val currencyId: UUID,
        val invoice: FileDB?,
        val countryId: UUID,
        val weight: Float?,
        val originalBox: Boolean,
    ) : DeliveryDTO

    data class UpdateDeliveryRequestDTO(
        val userId: UUID,
        val orderId: String,
        val trackingNumber: String?,
        val kazPostTrackNumber: String,
        val productLink: String,
        val name: String,
        val description: String,
        val deliveryPrice: Float?,
        val price: Float?,
        val currencyId: UUID,
        val invoice: FileDB?,
        val countryId: UUID,
        val weight: Float?,
        val originalBox: Boolean,
    ) : DeliveryDTO

    class GetDeliveryResponseDTO(
        val id: UUID?,
        val customOrderId: String,
        val kazPostTrackNumber: String?,
        val userId: UUID?,
        val status: DeliveryStatus,
        val trackingNumber: String?,
        val productLink: String,
        val deliveryPrice: Float?,
        val invoiceId: UUID?,
        val name: String,
        val description: String,
        val price: Float?,
        val currencyId: UUID,
        val countryId: UUID,
        val weight: Float?,
        val originalBox: Boolean,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
    ) : DeliveryDTO
}