package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Delivery
import kz.qpexpress.qpexpress.model.DeliveryStatus
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.model.Recipient
import java.util.*

sealed interface DeliveryDTO {
    data class CreateDeliveryRequestDTO(
        val userId: UUID,
        val recipient: Recipient,
        val goods: List<UUID>,
        val price: Float,
        val currencyId: UUID,
        val kazPostTrackNumber: String,
        val weight: Float,
        val invoice: FileDB,
    ) : DeliveryDTO

    data class UpdateDeliveryRequestDTO(
        val userId: UUID,
        val recipient: Recipient,
        val weight: Float,
        val kazPostTrackNumber: String?,
        val price: Float,
        val currencyId: UUID,
        val goods: List<UUID>,
        val invoice: FileDB,
    ) : DeliveryDTO

    data class UpdateDeliveryStatusRequestDTO(
        val status: DeliveryStatus
    ) : DeliveryDTO

    data class DeliveryResponseDTO(
        val id: UUID?,
        val userId: UUID,
        val recipient: RecipientDTO.RecipientResponseDTO,
        val goods: List<GoodDTO.GoodResponseDTO>,
        val price: Float,
        val currency: CurrencyDTO.CurrencyResponseDTO,
        val deliveryNumber: String,
        val status: DeliveryStatus,
        val kazPostTrackNumber: String?,
        val weight: Float,
        val invoice: FileDTO.FileResponseDTO?,
    ) : DeliveryDTO {
        constructor(delivery: Delivery) : this(
            id = delivery.id,
            userId = delivery.userId,
            recipient = RecipientDTO.RecipientResponseDTO(delivery.recipient),
            goods = delivery.goods.map { GoodDTO.GoodResponseDTO(it) },
            price = delivery.price,
            currency = CurrencyDTO.CurrencyResponseDTO(delivery.currency),
            deliveryNumber = delivery.deliveryNumber,
            status = delivery.status,
            kazPostTrackNumber = delivery.kazPostTrackNumber,
            weight = delivery.weight,
            invoice = delivery.invoice?.let { FileDTO.FileResponseDTO(it) }
        )
    }
}