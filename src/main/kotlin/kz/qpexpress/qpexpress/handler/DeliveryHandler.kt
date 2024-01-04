package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.DeliveryDTO
import kz.qpexpress.qpexpress.model.Delivery
import kz.qpexpress.qpexpress.model.DeliveryAction
import kz.qpexpress.qpexpress.model.DeliveryHistory
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
    private val deliveryHistoryRepository: DeliveryHistoryRepository,
    private val fileDBRepository: FileDBRepository,
    private val receiverRepository: ReceiverRepository,
): IDeliveryHandler {
    @PreAuthorize("hasAuthority('orders:write')")
    override fun createDelivery(data: DeliveryDTO.CreateDeliveryRequestDTO): ServerResponse {
        val foundCurrency = currencyRepository.findByIdOrNull(data.currencyId) ?: return ServerResponse.badRequest().build()
        val foundCountry = countryRepository.findByIdOrNull(data.countryId) ?: return ServerResponse.badRequest().build()
        val receiver = receiverRepository.findByUserId(data.userId)
        val savedInvoice = data.invoice?.let {
            fileDBRepository.save(it)
        }
        val delivery = data.let {
            Delivery().apply {
                userId = it.userId
                customDeliveryId = it.orderId
                name = it.name
                description = it.description
                trackingNumber = it.trackingNumber
                productLink = it.productLink
                price = it.price
                currency = foundCurrency
                country = foundCountry
                invoice = savedInvoice
                weight = it.weight
                originalBox = it.originalBox
            }
        }
        if (receiver == null) delivery.status = DeliveryStatus.DRAFT
        val result = deliveryRepository.save(delivery)
        val deliveryHistory = DeliveryHistory(result)
        deliveryHistoryRepository.save(deliveryHistory)
        val response = result.toOrderResponse()
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('orders:update')")
    override fun updateDelivery(data: DeliveryDTO.UpdateDeliveryRequestDTO, id: UUID): ServerResponse {
        val foundCurrency = currencyRepository.findByIdOrNull(data.currencyId) ?: return ServerResponse.badRequest().build()
        val foundCountry = countryRepository.findByIdOrNull(data.countryId) ?: return ServerResponse.badRequest().build()
        val order = deliveryRepository.findByIdOrNull(id) ?: return ServerResponse.badRequest().build()
        val savedInvoice = data.invoice?.let {
            fileDBRepository.save(it)
        }
        order.apply {
            userId = data.userId
            customDeliveryId = data.orderId
            name = data.name
            description = data.description
            trackingNumber = data.trackingNumber
            deliveryPrice = data.deliveryPrice
            kazPostTrackNumber = data.kazPostTrackNumber
            productLink = data.productLink
            price = data.price
            currency = foundCurrency
            country = foundCountry
            invoice = savedInvoice
            weight = data.weight
            originalBox = data.originalBox
        }
        val result = deliveryRepository.save(order)
        val deliveryHistory = DeliveryHistory(result).also {
            it.action = DeliveryAction.UPDATE
        }
        deliveryHistoryRepository.save(deliveryHistory)
        val response = result.toOrderResponse()
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('orders:delete')")
    override fun deleteDelivery(id: UUID): ServerResponse {
        TODO("Not yet implemented")
    }

    @PreAuthorize("hasAuthority('orders:read')")
    override fun getAllDeliveries(): ServerResponse {
        val orders = deliveryRepository.findAll()
        val response = orders.map {
            it.toOrderResponse()
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('orders:read')")
    override fun getDeliveryById(id: UUID): ServerResponse {
        val order = deliveryRepository.findById(id).get()
        val response = order.toOrderResponse()
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("isAuthenticated()")
    override fun getMyDeliveries(userId: UUID): ServerResponse {
        val orders = deliveryRepository.findAllByUserId(userId)
        val response = orders.map {
            it.toOrderResponse()
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("isAuthenticated()")
    override fun getMyDeliveryById(userId: UUID, deliveryId: UUID): ServerResponse {
        val order = deliveryRepository.findByIdAndUserId(deliveryId, userId) ?: return ServerResponse.notFound().build()
        val response = order.toOrderResponse()
        return ServerResponse.ok().body(response)
    }

    private fun Delivery.toOrderResponse(): DeliveryDTO.GetDeliveryResponseDTO {
        return DeliveryDTO.GetDeliveryResponseDTO(
            id = this.id,
            userId = this.userId,
            customOrderId = this.customDeliveryId,
            kazPostTrackNumber = this.kazPostTrackNumber,
            trackingNumber = this.trackingNumber,
            productLink = this.productLink,
            status = this.status,
            name = this.name,
            description = this.description,
            price = this.price,
            deliveryPrice = this.deliveryPrice,
            currencyId = this.currency.id!!,
            countryId = this.country.id!!,
            weight = this.weight,
            originalBox = this.originalBox,
            invoiceId = this.invoice?.id,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }
}