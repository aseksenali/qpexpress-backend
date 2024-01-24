package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.model.GoodStatus
import kz.qpexpress.qpexpress.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class GoodHandler(
    private val goodRepository: GoodRepository,
    private val countryRepository: CountryRepository,
    private val currencyRepository: CurrencyRepository,
    private val orderRepository: OrderRepository, private val recipientRepository: RecipientRepository,
) : IGoodHandler {
    override fun createGood(goodDTO: GoodDTO.CreateGoodDTO, orderId: UUID): ServerResponse {
        val country = countryRepository.findByIdOrNull(goodDTO.countryId) ?: return ServerResponse.notFound().build()
        val currency = currencyRepository.findByIdOrNull(goodDTO.currencyId) ?: return ServerResponse.notFound().build()
        val order = orderRepository.findByIdOrNull(orderId) ?: return ServerResponse.notFound().build()
        val recipient = recipientRepository.findByIdOrNull(goodDTO.recipientId)
            ?: return ServerResponse.notFound().build()
        val good = goodDTO.toGood(country, currency, order, recipient, null)
        val result = goodRepository.save(good)
        return ServerResponse.ok().body(GoodDTO.GoodResponseDTO(result))
    }

    override fun getGoods(userId: UUID?, recipientId: UUID?, orderId: UUID?, deliveryId: UUID?, status: GoodStatus?): ServerResponse {
        val specification = GoodSpecification.byAll(userId = userId, orderId = orderId, deliveryId = deliveryId, status = status, recipientId = recipientId)
        val result = goodRepository.findAll(specification).map { GoodDTO.GoodResponseDTO(it) }
        return ServerResponse.ok().body(result)
    }
}