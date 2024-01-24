package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.*
import kz.qpexpress.qpexpress.model.Currency
import org.springframework.http.MediaType
import java.util.*

sealed interface GoodDTO {
    data class CreateGoodDTO(
        val name: String,
        val description: String,
        val customOrderId: String,
        val countryId: UUID,
        val link: String,
        val price: Float,
        val currencyId: UUID,
        val invoiceId: UUID?,
        val trackingNumber: String?,
        val recipientId: UUID,
        val originalBox: Boolean,
        val quantity: Int,
        val userId: UUID,
    ) {
        fun toGood(country: Country, currency: Currency, order: Order, recipient: Recipient, invoice: FileDB?): Good {
            return Good().also {
                it.name = this.name
                it.customOrderId = this.customOrderId
                it.description = this.description
                it.order = order
                it.country = country
                it.link = this.link
                it.userId = userId
                it.price = this.price
                it.currency = currency
                it.invoice = invoice
                it.trackingNumber = this.trackingNumber
                it.originalBox = this.originalBox
                it.quantity = this.quantity
                it.recipient = recipient
                it.status = GoodStatus.CREATED
            }
        }
    }

    data class GoodResponseDTO(
        val id: UUID,
        val name: String,
        val customOrderId: String,
        val description: String,
        val country: Country,
        val link: String,
        val price: Float,
        val currency: Currency,
        val invoice: FileDTO.FileResponseDTO?,
        val trackingNumber: String?,
        val originalBox: Boolean,
        val quantity: Int,
        val userId: UUID,
        val recipientId: UUID,
        val status: GoodStatus,
        val orderId: UUID,
        val deliveryId: UUID?,
    ) {
        constructor(good: Good) : this(
            id = good.id!!,
            name = good.name,
            customOrderId = good.customOrderId,
            description = good.description,
            country = good.country,
            link = good.link,
            price = good.price,
            currency = good.currency,
            invoice = good.invoice?.let { FileDTO.FileResponseDTO(it.id!!, it.name, MediaType.parseMediaType(it.contentType)) },
            trackingNumber = good.trackingNumber,
            originalBox = good.originalBox,
            quantity = good.quantity,
            userId = good.userId,
            recipientId = good.recipient.id!!,
            status = good.status,
            orderId = good.order.id!!,
            deliveryId = good.delivery?.id,
        )
    }
}