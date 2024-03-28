package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Address
import kz.qpexpress.qpexpress.model.Language
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AddressRepository: JpaRepository<Address, UUID> {
    fun findAllByLanguage(language: Language): List<Address>
}