package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.DeliveryHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeliveryHistoryRepository: JpaRepository<DeliveryHistory, UUID> {
}