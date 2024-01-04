package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Currency
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CurrencyRepository: JpaRepository<Currency, UUID> {
}