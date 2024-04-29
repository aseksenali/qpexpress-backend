package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Currency
import java.util.*

sealed interface CurrencyDTO {
    data class CreateCurrencyDTO(
        val nameRus: String,
        val nameKaz: String,
        val nameChn: String,
        val nameEng: String,
        val code: String,
    ) : CurrencyDTO {
        fun toEntity(): Currency {
            val currency = Currency()
            currency.nameRus = nameRus
            currency.nameKaz = nameKaz
            currency.nameChn = nameChn
            currency.nameEng = nameEng
            currency.code = code
            return currency
        }
    }

    data class CurrencyResponseDTO(
        val id: UUID,
        val nameRus: String,
        val nameKaz: String,
        val nameChn: String,
        val nameEng: String,
    ) : CurrencyDTO {
        constructor(currency: Currency) : this(
            id = currency.id!!,
            nameRus = currency.nameRus,
            nameKaz = currency.nameKaz,
            nameChn = currency.nameChn,
            nameEng = currency.nameEng
        )
    }
}