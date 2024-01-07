package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, UUID> {
    fun findAllByUserId(userId: UUID): List<Order>
}