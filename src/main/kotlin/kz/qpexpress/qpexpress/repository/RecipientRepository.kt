package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Recipient
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RecipientRepository: JpaRepository<Recipient, UUID> {
    fun findByUserId(userId: UUID): Recipient?
}