package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.model.GoodStatus
import kz.qpexpress.qpexpress.repository.CountryRepository
import kz.qpexpress.qpexpress.repository.CurrencyRepository
import kz.qpexpress.qpexpress.repository.GoodRepository
import kz.qpexpress.qpexpress.repository.OrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class GoodHandler(
    private val goodRepository: GoodRepository,
    private val countryRepository: CountryRepository,
    private val currencyRepository: CurrencyRepository,
    private val orderRepository: OrderRepository,
): IGoodHandler {
    override fun createGood(goodDTO: GoodDTO.CreateGoodDTO): ServerResponse {
        val country = countryRepository.findByIdOrNull(goodDTO.countryId) ?: return ServerResponse.notFound().build()
        val currency = currencyRepository.findByIdOrNull(goodDTO.currencyId) ?: return ServerResponse.notFound().build()
        val order = orderRepository.findByIdOrNull(goodDTO.orderId) ?: return ServerResponse.notFound().build()
        val good = goodDTO.toGood(country, currency, order)
        val result = goodRepository.save(good)
        return ServerResponse.ok().body(result)
    }

    override fun getGoodsByDeliveryId(deliveryId: UUID): ServerResponse {
        val result = goodRepository.findAllByDeliveryId(deliveryId)
        return ServerResponse.ok().body(result)
    }

    override fun getGoodsByOrderId(orderId: UUID): ServerResponse {
        val result = goodRepository.findAllByOrderId(orderId)
        return ServerResponse.ok().body(result)
    }

    override fun getGoodsByUserId(userId: UUID): ServerResponse {
        val result = goodRepository.findAllByUserId(userId)
        return ServerResponse.ok().body(result)
    }

    override fun getGoodsByUserIdAndStatus(userId: UUID, status: GoodStatus): ServerResponse {
        val result = goodRepository.findAllByUserIdAndStatus(userId, status)
        return ServerResponse.ok().body(result)
    }
}