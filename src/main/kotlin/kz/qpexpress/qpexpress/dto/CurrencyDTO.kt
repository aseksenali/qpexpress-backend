package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Currency
import java.util.*

sealed interface CurrencyDTO {
    data class CreateCurrencyDTO(
        val nameRus: String,
        val nameChn: String,
        val nameEng: String
    ) : CurrencyDTO {
        fun toEntity(): Currency {
            val currency = Currency()
            currency.nameRus = nameRus
            currency.nameChn = nameChn
            currency.nameEng = nameEng
            return currency
        }
    }

    data class CurrencyResponseDTO(
        val id: UUID,
        val nameRus: String,
        val nameChn: String,
        val nameEng: String
    ) : CurrencyDTO {
        constructor(currency: Currency) : this(
            id = currency.id!!,
            nameRus = currency.nameRus,
            nameChn = currency.nameChn,
            nameEng = currency.nameEng
        )
    }
}