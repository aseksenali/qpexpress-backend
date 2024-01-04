package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Country
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CountryRepository: JpaRepository<Country, UUID> {
}