package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.Good
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface GoodRepository: JpaRepository<Good, UUID>, JpaSpecificationExecutor<Good> {
    fun findAllByDeliveryId(deliveryId: UUID): List<Good>
}