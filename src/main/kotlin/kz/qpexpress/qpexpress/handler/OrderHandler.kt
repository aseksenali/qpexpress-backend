package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.dto.OrderDTO
import kz.qpexpress.qpexpress.model.Good
import kz.qpexpress.qpexpress.model.OrderStatus
import kz.qpexpress.qpexpress.model.Recipient
import kz.qpexpress.qpexpress.repository.CountryRepository
import kz.qpexpress.qpexpress.repository.CurrencyRepository
import kz.qpexpress.qpexpress.repository.OrderRepository
import kz.qpexpress.qpexpress.repository.RecipientRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class OrderHandler(
    private val orderRepository: OrderRepository,
    private val countryRepository: CountryRepository,
    private val currencyRepository: CurrencyRepository,
    private val recipientRepository: RecipientRepository,
) : IOrderHandler {
    private fun getRecipientAndGoods(recipientId: UUID, goodDTOs: List<GoodDTO.CreateGoodDTO>): Pair<Recipient, MutableSet<Good>>? {
        val recipient = recipientRepository.findByIdOrNull(recipientId) ?: return null
        val goods = goodDTOs.map {
            val country = countryRepository.findByIdOrNull(it.countryId) ?: return null
            val currency = currencyRepository.findByIdOrNull(it.currencyId) ?: return null
            val order = orderRepository.findByIdOrNull(it.orderId) ?: return null
            it.toGood(country, currency, order)
        }.toMutableSet()
        return Pair(recipient, goods)
    }

    override fun createOrder(data: OrderDTO.CreateOrderDTO): ServerResponse {
        val (recipient, goods) = getRecipientAndGoods(data.recipientId, data.goods) ?: return ServerResponse.notFound().build()
        val result = orderRepository.save(data.toOrder(recipient, goods))
        return ServerResponse.ok().body(result)
    }

    override fun getOrderById(id: UUID): ServerResponse {
        val result = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        return ServerResponse.ok().body(result)
    }

    override fun updateOrder(id: UUID, data: OrderDTO.UpdateOrderDTO): ServerResponse {
        val (recipient, goods) = getRecipientAndGoods(data.recipientId, data.goods) ?: return ServerResponse.notFound().build()
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        order.apply {
            this.recipient = recipient
            this.goods = goods
            this.status = data.status
        }
        val result = orderRepository.save(order)
        return ServerResponse.ok().body(result)
    }

    override fun deleteOrder(id: UUID): ServerResponse {
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        order.status = OrderStatus.DELETED
        orderRepository.save(order)
        return ServerResponse.ok().build()
    }

    override fun getOrders(): ServerResponse {
        val result = orderRepository.findAll()
        return ServerResponse.ok().body(result)
    }

    override fun getMyOrders(userId: UUID): ServerResponse {
        val result = orderRepository.findAllByUserId(userId)
        return ServerResponse.ok().body(result)
    }

    override fun updateMyOrder(id: UUID, userId: UUID, data: OrderDTO.UpdateMyOrderDTO): ServerResponse {
        val (recipient, goods) = getRecipientAndGoods(data.recipientId, data.goods) ?: return ServerResponse.notFound().build()
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        if (userId != order.userId) return ServerResponse.notFound().build()
        order.apply {
            this.recipient = recipient
            this.goods = goods
        }
        val result = orderRepository.save(order)
        return ServerResponse.ok().body(result)
    }

    override fun deleteMyOrder(id: UUID, userId: UUID): ServerResponse {
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        if (userId != order.userId) return ServerResponse.notFound().build()
        order.status = OrderStatus.DELETED
        orderRepository.save(order)
        return ServerResponse.ok().build()
    }

}