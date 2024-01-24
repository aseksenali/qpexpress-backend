package kz.qpexpress.qpexpress.handler

import kotlin.random.Random
import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.dto.OrderDTO
import kz.qpexpress.qpexpress.model.Good
import kz.qpexpress.qpexpress.model.Order
import kz.qpexpress.qpexpress.model.OrderStatus
import kz.qpexpress.qpexpress.model.Recipient
import kz.qpexpress.qpexpress.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class OrderHandler(
    private val orderRepository: OrderRepository,
    private val goodRepository: GoodRepository,
    private val countryRepository: CountryRepository,
    private val currencyRepository: CurrencyRepository,
    private val recipientRepository: RecipientRepository,
    private val fileDBRepository: FileDBRepository,
) : IOrderHandler {
    private fun getRecipientAndGoods(
        recipientId: UUID,
        goodDTOs: List<GoodDTO.CreateGoodDTO>,
        order: Order
    ): Pair<Recipient, MutableSet<Good>>? {
        val recipient = recipientRepository.findByIdOrNull(recipientId) ?: return null
        val goods = goodDTOs.map {
            val country = countryRepository.findByIdOrNull(it.countryId) ?: return null
            val currency = currencyRepository.findByIdOrNull(it.currencyId) ?: return null
            val invoice = it.invoiceId?.let { fileId -> fileDBRepository.findByIdOrNull(fileId) }
            it.toGood(country, currency, order, recipient, invoice)
        }.toMutableSet()
        return Pair(recipient, goods)
    }

    override fun createOrder(userId: UUID, data: OrderDTO.CreateOrderDTO): ServerResponse {
        val recipient = recipientRepository.findByIdOrNull(data.recipientId) ?: return ServerResponse.notFound().build()
        val order = Order().also {
            it.recipient = recipient
            it.status = OrderStatus.CREATED
            it.orderNumber = generateOrderId()
            it.userId = userId
        }
        val orderResult = orderRepository.save(order)
        val goods = data.goods.map {
            val country = countryRepository.findByIdOrNull(it.countryId) ?: return ServerResponse.notFound().build()
            val currency = currencyRepository.findByIdOrNull(it.currencyId) ?: return ServerResponse.notFound().build()
            val invoice = it.invoiceId?.let { fileId -> fileDBRepository.findByIdOrNull(fileId) }
            it.toGood(country, currency, orderResult, recipient, invoice)
        }.toMutableSet()
        goods.forEach(goodRepository::save)
        return ServerResponse.ok().body(OrderDTO.OrderResponseDTO(order.let {
            it.goods = goods
            it
        }))
    }

    override fun getOrderById(id: UUID): ServerResponse {
        val result = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        val goods = goodRepository.findAll(GoodSpecification.byOrderId(result.id!!)!!)
        result.goods = goods.toMutableSet()
        return ServerResponse.ok().body(OrderDTO.OrderResponseDTO(result))
    }

    private fun generateOrderId(): String {
        val letters = ('A'..'Z').toList()
        val prefix = List(3) { letters.random() }.joinToString("")
        val suffix = List(10) { Random.nextInt(0, 10) }.joinToString("")
        return "$prefix-$suffix"
    }

    override fun updateOrder(id: UUID, data: OrderDTO.UpdateOrderDTO): ServerResponse {
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        val (recipient, goods) = getRecipientAndGoods(data.recipientId, data.goods, order) ?: return ServerResponse.notFound()
            .build()
        order.apply {
            this.recipient = recipient
            this.goods = goods
            this.status = data.status
        }
        val result = orderRepository.save(order)
        return ServerResponse.ok().body(OrderDTO.OrderResponseDTO(result))
    }

    override fun deleteOrder(id: UUID): ServerResponse {
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        order.status = OrderStatus.DELETED
        orderRepository.save(order)
        return ServerResponse.ok().build()
    }

    override fun getOrders(): ServerResponse {
        val result = orderRepository.findAll().map { OrderDTO.OrderResponseDTO(it) }
        return ServerResponse.ok().body(result)
    }

    override fun getMyOrders(userId: UUID): ServerResponse {
        val result = orderRepository.findAllByUserId(userId).map { OrderDTO.OrderResponseDTO(it) }
        return ServerResponse.ok().body(result)
    }

    override fun updateMyOrder(id: UUID, userId: UUID, data: OrderDTO.UpdateMyOrderDTO): ServerResponse {
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        val (recipient, goods) = getRecipientAndGoods(data.recipientId, data.goods, order) ?: return ServerResponse.notFound()
            .build()
        if (userId != order.userId) return ServerResponse.notFound().build()
        order.apply {
            this.recipient = recipient
            this.goods = goods
        }
        val result = orderRepository.save(order)
        return ServerResponse.ok().body(OrderDTO.OrderResponseDTO(result))
    }

    override fun deleteMyOrder(id: UUID, userId: UUID): ServerResponse {
        val order = orderRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        if (userId != order.userId) return ServerResponse.notFound().build()
        order.status = OrderStatus.DELETED
        orderRepository.save(order)
        return ServerResponse.ok().build()
    }
}