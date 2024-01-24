package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Currency
import java.util.*

sealed interface CurrencyDTO {
    data class CreateCurrencyDTO(
        val name: String
    ) : CurrencyDTO {
        fun toEntity(): Currency {
            val currency = Currency()
            currency.name = name
            return currency
        }
    }

    data class CurrencyResponseDTO(
        val id: UUID,
        val name: String,
    ) : CurrencyDTO {
        constructor(currency: Currency) : this(
            id = currency.id!!,
            name = currency.name
        )
    }
}