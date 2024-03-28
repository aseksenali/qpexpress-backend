package kz.qpexpress.qpexpress.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.Part
import kz.qpexpress.qpexpress.dto.DeliveryDTO
import kz.qpexpress.qpexpress.handler.IDeliveryHandler
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.repository.FileDBRepository
import kz.qpexpress.qpexpress.repository.RecipientRepository
import kz.qpexpress.qpexpress.util.toFileDB
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.util.*

data class MoneyDTO(
    val value: Float,
    val currencyId: UUID
)

@Service
class DeliveryService(
    private val deliveryHandler: IDeliveryHandler,
    private val fileDBRepository: FileDBRepository,
    private val recipientRepository: RecipientRepository
) : IDeliveryService {
    private val mapper = jacksonObjectMapper()
    private fun Part.decodeToString() = inputStream.bufferedReader().use { it.readText() }

    private fun MultiValueMap<String, Part>.getFileDBOrNull(
        fieldName: String,
        servletRequest: MultipartHttpServletRequest
    ): FileDB? {
        if (this["${fieldName}Id"] != null) {
            val id = this["${fieldName}Id"]?.firstOrNull()?.decodeToString()?.toUUID()
            if (id != null) {
                val fileDB = fileDBRepository.findByIdOrNull(id)
                return fileDB
            }
        } else {
            return servletRequest.getFile(fieldName)?.toFileDB()
        }
        return null
    }

    override fun getAllDeliveries(request: ServerRequest): ServerResponse {
        return deliveryHandler.getAllDeliveries()
    }

    override fun getDeliveryById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return deliveryHandler.getDeliveryById(id)
    }

    override fun createDelivery(request: ServerRequest): ServerResponse {
        val data = request.toDeliveryDTO<DeliveryDTO.CreateDeliveryRequestDTO>()
        return deliveryHandler.createDelivery(data)
    }

    override fun updateDelivery(request: ServerRequest): ServerResponse {
        val data = request.toDeliveryDTO<DeliveryDTO.UpdateDeliveryRequestDTO>()
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return deliveryHandler.updateDelivery(data, id)
    }

    override fun updateDeliveryStatus(request: ServerRequest): ServerResponse {
        val requestData = request.body<DeliveryDTO.UpdateDeliveryStatusRequestDTO>()
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return deliveryHandler.updateDeliveryStatus(requestData, id)
    }

    override fun deleteDelivery(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return deliveryHandler.deleteDelivery(id)
    }

    override fun getMyDeliveries(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        return deliveryHandler.getMyDeliveries(userId)
    }

    override fun getMyDeliveryById(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        val orderId = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return deliveryHandler.getMyDeliveryById(userId, orderId)
    }

    private inline fun <reified T> ServerRequest.toDeliveryDTO(): T where T : DeliveryDTO {
        val multipartData = this.multipartData()
        val singleData = multipartData.toSingleValueMap()
        val multipartRequest = this.servletRequest() as MultipartHttpServletRequest
        val invoice = multipartData.getFileDBOrNull("invoice", multipartRequest)
        if (invoice == null) {
            throw Exception("Photo2 is null")
        }
        val price = singleData["price"]?.decodeToString()?.toFloat() ?: throw Exception("price is null")
        val currencyId =
            singleData["currencyId"]?.decodeToString()?.toUUID() ?: throw Exception("currencyId is null")
        val weight = singleData["weight"]?.decodeToString()?.toFloat() ?: throw Exception("weight is null")
        val kazPostTrackNumber = singleData["kazPostTrackNumber"]?.decodeToString() ?: throw Exception("kazPostTrackNumber is null")
        val productsJson = singleData["products"]?.decodeToString() ?: throw Exception("products is null")
        val products = mapper.readValue<List<UUID>>(productsJson)
        val recipientId = singleData["recipientId"]?.decodeToString()?.toUUID() ?: throw Exception("recipientId is null")
        val recipient = recipientRepository.findByIdOrNull(recipientId) ?: throw Exception("recipient is null")
        when (T::class.java) {
            DeliveryDTO.CreateDeliveryRequestDTO::class.java -> {
                return DeliveryDTO.CreateDeliveryRequestDTO(
                    userId = recipient.userId,
                    goods = products,
                    price = price,
                    currencyId = currencyId,
                    kazPostTrackNumber = kazPostTrackNumber,
                    weight = weight,
                    invoice = invoice,
                    recipient = recipient
                ) as T
            }

            DeliveryDTO.UpdateDeliveryRequestDTO::class.java -> {
                return DeliveryDTO.UpdateDeliveryRequestDTO(
                    userId = recipient.userId,
                    goods = products,
                    price = price,
                    currencyId = currencyId,
                    kazPostTrackNumber = kazPostTrackNumber,
                    weight = weight,
                    invoice = invoice,
                    recipient = recipient
                ) as T
            }

            else -> throw Exception("Unknown class")
        }
    }
}