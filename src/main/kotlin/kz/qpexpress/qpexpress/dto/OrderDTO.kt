package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Good
import kz.qpexpress.qpexpress.model.Order
import kz.qpexpress.qpexpress.model.OrderStatus
import kz.qpexpress.qpexpress.model.Recipient
import java.util.*

sealed interface OrderDTO {
    data class CreateOrderDTO(
        val recipientId: UUID,
        val goods: List<GoodDTO.CreateGoodDTO>,
    ) : OrderDTO

    data class UpdateOrderDTO(
        val recipientId: UUID,
        val status: OrderStatus,
        val goods: List<GoodDTO.CreateGoodDTO>,
    ) : OrderDTO {
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
    ) : OrderDTO {
        fun toOrder(recipient: Recipient, goods: MutableSet<Good>): Order {
            return Order().also {
                it.recipient = recipient
                it.goods = goods
            }
        }
    }

    data class OrderResponseDTO(
        val id: UUID,
        val recipient: RecipientDTO.RecipientResponseDTO,
        val goods: List<GoodDTO.GoodResponseDTO>,
        val status: OrderStatus,
        val orderNumber: String,
    ) : OrderDTO {
        constructor(order: Order) : this(
            id = order.id!!,
            recipient = RecipientDTO.RecipientResponseDTO(order.recipient),
            goods = order.goods.map { GoodDTO.GoodResponseDTO(it) },
            status = order.status,
            orderNumber = order.orderNumber,
        )
    }
}