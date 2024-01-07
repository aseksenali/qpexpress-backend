package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Good
import kz.qpexpress.qpexpress.model.GoodStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GoodRepository: JpaRepository<Good, UUID> {
    fun findAllByDeliveryId(deliveryId: UUID): List<Good>
    fun findAllByOrderId(orderId: UUID): List<Good>
    fun findAllByUserId(userId: UUID): List<Good>
    fun findAllByUserIdAndStatus(userId: UUID, status: GoodStatus): List<Good>
}