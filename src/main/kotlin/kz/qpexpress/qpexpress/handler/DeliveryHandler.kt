package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.DeliveryDTO
import kz.qpexpress.qpexpress.model.Delivery
import kz.qpexpress.qpexpress.model.DeliveryStatus
import kz.qpexpress.qpexpress.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class DeliveryHandler(
    private val deliveryRepository: DeliveryRepository,
    private val currencyRepository: CurrencyRepository,
    private val countryRepository: CountryRepository,
    private val fileDBRepository: FileDBRepository,
    private val recipientRepository: RecipientRepository,
    private val goodRepository: GoodRepository,
) : IDeliveryHandler {
    @PreAuthorize("hasAuthority('deliveries:write')")
    override fun createDelivery(data: DeliveryDTO.CreateDeliveryRequestDTO): ServerResponse {
        val goods = data.goods.map {
            goodRepository.findByIdOrNull(it) ?: return ServerResponse.notFound().build()
        }.toMutableSet()
        val delivery = Delivery().apply {
            this.userId = data.userId
            this.goods = goods
        }
        val result = deliveryRepository.save(delivery)
        return ServerResponse.ok().body(result)
    }

    @PreAuthorize("hasAuthority('deliveries:update')")
    override fun updateDelivery(data: DeliveryDTO.UpdateDeliveryRequestDTO, id: UUID): ServerResponse {
        val goods = data.goods.map {
            goodRepository.findByIdOrNull(it) ?: return ServerResponse.notFound().build()
        }.toMutableSet()
        val delivery = deliveryRepository.findByIdOrNull(id) ?: return ServerResponse.badRequest().build()
        delivery.apply {
            this.userId = data.userId
            this.kazPostTrackNumber = data.kazPostTrackNumber
            this.price = data.price
            this.weight = data.weight
            this.goods = goods
        }
        val result = deliveryRepository.save(delivery)
        return ServerResponse.ok().body(result)
    }

    @PreAuthorize("hasAuthority('deliveries:delete')")
    override fun deleteDelivery(id: UUID): ServerResponse {
        val delivery = deliveryRepository.findByIdOrNull(id) ?: return ServerResponse.badRequest().build()
        delivery.apply {
            status = DeliveryStatus.DELETED
        }
        val result = deliveryRepository.save(delivery)
        return ServerResponse.ok().body(result)
    }

    @PreAuthorize("hasAuthority('deliveries:read')")
    override fun getAllDeliveries(): ServerResponse {
        val orders = deliveryRepository.findAll()
        return ServerResponse.ok().body(orders)
    }

    @PreAuthorize("hasAuthority('deliveries:read')")
    override fun getDeliveryById(id: UUID): ServerResponse {
        val order = deliveryRepository.findById(id).get()
        return ServerResponse.ok().body(order)
    }

    @PreAuthorize("isAuthenticated()")
    override fun getMyDeliveries(userId: UUID): ServerResponse {
        val orders = deliveryRepository.findAllByUserId(userId)
        return ServerResponse.ok().body(orders)
    }

    @PreAuthorize("isAuthenticated()")
    override fun getMyDeliveryById(userId: UUID, deliveryId: UUID): ServerResponse {
        val order = deliveryRepository.findByIdAndUserId(deliveryId, userId) ?: return ServerResponse.notFound().build()
        return ServerResponse.ok().body(order)
    }
}