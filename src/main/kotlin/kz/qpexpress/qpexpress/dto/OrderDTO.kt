package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Good
import kz.qpexpress.qpexpress.model.Order
import kz.qpexpress.qpexpress.model.OrderStatus
import kz.qpexpress.qpexpress.model.Recipient
import java.util.*

sealed interface OrderDTO {
    data class CreateOrderDTO(
        val userId: UUID,
        val recipientId: UUID,
        val goods: List<GoodDTO.CreateGoodDTO>,
    ) {
        fun toOrder(recipient: Recipient, goods: MutableSet<Good>): Order {
            return Order().also {
                it.userId = this.userId
                it.recipient = recipient
                it.goods = goods
            }
        }
    }

    data class UpdateOrderDTO(
        val recipientId: UUID,
        val status: OrderStatus,
        val goods: List<GoodDTO.CreateGoodDTO>,
    ) {
        fun toOrder(recipient: Recipient, goods: MutableSet<Good>): Order {
            return Order().also {
                it.recipient = recipient
                it.goods = goods
            }
        }
    }

    data class UpdateMyOrderDTO(
        val recipientId: UUID,
        val goods: List<GoodDTO.CreateGoodDTO>,
    ) {
        fun toOrder(recipient: Recipient, goods: MutableSet<Good>): Order {
            return Order().also {
                it.recipient = recipient
                it.goods = goods
            }
        }
    }
}