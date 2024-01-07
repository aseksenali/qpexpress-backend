package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.*
import kz.qpexpress.qpexpress.model.Currency
import java.util.*

sealed interface GoodDTO {
    data class CreateGoodDTO(
        val name: String,
        val orderId: UUID,
        val description: String,
        val countryId: UUID,
        val customOrderId: String,
        val link: String,
        val price: Float,
        val currencyId: UUID,
        val invoice: FileDB?,
        val trackingNumber: String?,
        val originalBox: Boolean,
        val quantity: Int,
        val userId: UUID,
    ) {
        fun toGood(country: Country, currency: Currency, order: Order): Good {
            return Good().also {
                it.name = this.name
                it.description = this.description
                it.order = order
                it.country = country
                it.customOrderId = this.customOrderId
                it.link = this.link
                it.userId = userId
                it.price = this.price
                it.currency = currency
                it.invoice = this.invoice
                it.trackingNumber = this.trackingNumber
                it.originalBox = this.originalBox
                it.quantity = this.quantity
            }
        }
    }
}