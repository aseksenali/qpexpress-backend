package kz.qpexpress.qpexpress.handler

import kotlin.random.Random
import kz.qpexpress.qpexpress.dto.DeliveryDTO
import kz.qpexpress.qpexpress.model.Delivery
import kz.qpexpress.qpexpress.model.DeliveryStatus
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.model.GoodStatus
import kz.qpexpress.qpexpress.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class DeliveryHandler(
    private val deliveryRepository: DeliveryRepository,
    private val currencyRepository: CurrencyRepository,
    private val goodRepository: GoodRepository,
    private val fileDBRepository: FileDBRepository,
) : IDeliveryHandler {
    @PreAuthorize("hasAuthority('deliveries:write')")
    override fun createDelivery(data: DeliveryDTO.CreateDeliveryRequestDTO): ServerResponse {
        val goods = data.goods.map {
            goodRepository.findByIdOrNull(it) ?: return ServerResponse.notFound().build()
        }.toMutableSet()
        val currency = currencyRepository.findByIdOrNull(data.currencyId) ?: return ServerResponse.notFound().build()
        val fileDB = FileDB().apply {
            this.name = data.invoice.name
            this.contentType = data.invoice.contentType
            this.data = data.invoice.data
        }
        val savedFile = fileDBRepository.save(fileDB)
        val delivery = Delivery().apply {
            this.userId = data.userId
            this.recipient = data.recipient
            this.goods = goods
            this.currency = currency
            this.weight = data.weight
            this.kazPostTrackNumber = data.kazPostTrackNumber
            this.payed = false
            this.price = data.price
            this.status = DeliveryStatus.IN_THE_WAY
            this.invoice = savedFile
            this.deliveryNumber = generateDeliveryNumber()
        }
        val result = deliveryRepository.save(delivery)
        delivery.goods.forEach {
            it.status = GoodStatus.WAITING_FOR_PAYMENT
            it.delivery = result
            goodRepository.save(it)
        }
        val response = DeliveryDTO.DeliveryResponseDTO(result)
        return ServerResponse.ok().body(response)
    }

    @Transactional
    @PreAuthorize("hasAuthority('deliveries:write')")
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
        val response = DeliveryDTO.DeliveryResponseDTO(result)
        return ServerResponse.ok().body(response)
    }

    @Transactional
    override fun updateDeliveryStatus(data: DeliveryDTO.UpdateDeliveryStatusRequestDTO, id: UUID): ServerResponse {
        val delivery = deliveryRepository.findByIdOrNull(id) ?: return ServerResponse.badRequest().build()
        delivery.apply {
            status = data.status
        }
        val result = deliveryRepository.save(delivery)
        val response = DeliveryDTO.DeliveryResponseDTO(result)
        return ServerResponse.ok().body(response)
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
        val deliveries = deliveryRepository.findAll().map { DeliveryDTO.DeliveryResponseDTO(it) }
        return ServerResponse.ok().body(deliveries)
    }

    @PreAuthorize("hasAuthority('deliveries:read')")
    override fun getDeliveryById(id: UUID): ServerResponse {
        val delivery = deliveryRepository.findById(id).get()
        return ServerResponse.ok().body(DeliveryDTO.DeliveryResponseDTO(delivery))
    }

    @PreAuthorize("isAuthenticated()")
    override fun getMyDeliveries(userId: UUID): ServerResponse {
        val deliveries = deliveryRepository.findAllByUserId(userId).map { DeliveryDTO.DeliveryResponseDTO(it) }
        return ServerResponse.ok().body(deliveries)
    }

    @PreAuthorize("isAuthenticated()")
    override fun getMyDeliveryById(userId: UUID, deliveryId: UUID): ServerResponse {
        val delivery = deliveryRepository.findByIdAndUserId(deliveryId, userId) ?: return ServerResponse.notFound().build()
        return ServerResponse.ok().body(DeliveryDTO.DeliveryResponseDTO(delivery))
    }

    private val generateDeliveryNumber: () -> String = {
        val letters = ('A'..'Z').toList()
        val prefix = List(3) { letters.random() }.joinToString("")
        val suffix = List(10) { Random.nextInt(0, 10) }.joinToString("")
        "$prefix-$suffix"
    }
}