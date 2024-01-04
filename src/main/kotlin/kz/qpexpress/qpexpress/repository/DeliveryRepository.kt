package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Delivery
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeliveryRepository: JpaRepository<Delivery, UUID> {
    fun findAllByUserId(userId: UUID): List<Delivery>
    fun findByIdAndUserId(id: UUID, userId: UUID): Delivery?
}