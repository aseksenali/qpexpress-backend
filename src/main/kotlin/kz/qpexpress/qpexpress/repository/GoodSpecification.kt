package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.*
import org.springframework.data.jpa.domain.Specification
import java.util.*

object GoodSpecification {
    private fun byUserId(userId: UUID?): Specification<Good>? {
        return if (userId != null) {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("userId"), userId)
            }
        } else null
    }

    public fun byOrderId(orderId: UUID?): Specification<Good>? {
        return if (orderId != null) {
            Specification { root, _, cb ->
                cb.equal(root.get<Order>("order").get<UUID>("id"), orderId)
            }
        } else null
    }

    private fun byDeliveryId(deliveryId: UUID?): Specification<Good>? {
        return if (deliveryId != null) {
            Specification { root, _, cb ->
                cb.equal(root.get<Delivery>("delivery").get<UUID>("id"), deliveryId)
            }
        } else null
    }

    private fun byStatus(status: GoodStatus?): Specification<Good>? {
        return if (status != null) {
            Specification { root, _, cb ->
                cb.equal(root.get<GoodStatus>("status"), status)
            }
        } else null
    }

    private fun byRecipientId(recipientId: UUID?): Specification<Good>? {
        return if (recipientId != null) {
            Specification { root, _, cb ->
                cb.equal(root.get<Recipient>("recipient").get<UUID>("id"), recipientId)
            }
        } else null
    }

    fun byAll(userId: UUID?, recipientId: UUID?, orderId: UUID?, deliveryId: UUID?, status: GoodStatus?): Specification<Good> {
        val specs = listOfNotNull(byUserId(userId), byOrderId(orderId), byDeliveryId(deliveryId), byStatus(status), byRecipientId(recipientId))
        return if (specs.isNotEmpty()) {
            specs.reduce { acc, spec -> acc.and(spec) }
        } else {
            Specification { _, _, _ -> null }
        }
    }
}