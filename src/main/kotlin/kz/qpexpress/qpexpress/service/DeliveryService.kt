package kz.qpexpress.qpexpress.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.Part
import kz.qpexpress.qpexpress.dto.DeliveryDTO
import kz.qpexpress.qpexpress.handler.DeliveryHandler
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.repository.FileDBRepository
import kz.qpexpress.qpexpress.repository.RecipientRepository
import kz.qpexpress.qpexpress.util.toUUID
import org.hibernate.engine.jdbc.BlobProxy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
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
    private val deliveryHandler: DeliveryHandler,
    private val fileDBRepository: FileDBRepository,
    private val recipientRepository: RecipientRepository
) : IDeliveryService {
    private val mapper = jacksonObjectMapper()
    private fun Part.decodeToString() = inputStream.bufferedReader().use { it.readText() }

    override fun getAllDeliveries(request: ServerRequest): ServerResponse {
        return deliveryHandler.getAllDeliveries()
    }

    override fun getDeliveryById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return deliveryHandler.getDeliveryById(id)
    }

    override fun createDelivery(request: ServerRequest): ServerResponse {
        val multipartData = request.multipartData()
        val singleData = multipartData.toSingleValueMap()
        val invoiceField = multipartData["invoice"]
        val invoice = if (invoiceField != null) {
            val multipartRequest = request.servletRequest() as MultipartHttpServletRequest
            val file = multipartRequest.getFile("invoice")
            if (file == null) return ServerResponse.badRequest().build()
            else {
                val fileName = file.originalFilename ?: UUID.randomUUID().toString()
                val contentType = file.contentType
                val invoiceData = BlobProxy.generateProxy(file.inputStream, file.size)
                val fileEntity = FileDB().also {
                    it.name = fileName
                    it.contentType = contentType.toString()
                    it.data = invoiceData
                }
                fileDBRepository.save(fileEntity)
            }
        } else return ServerResponse.badRequest().build()
        val price = singleData["price"]?.decodeToString()?.toFloat() ?: return ServerResponse.badRequest().build()
        val currencyId =
            singleData["currencyId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val weight = singleData["weight"]?.decodeToString()?.toFloat() ?: return ServerResponse.badRequest().build()
        val kazPostTrackNumber = singleData["kazPostTrackNumber"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val productsJson = singleData["products"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val products = mapper.readValue<List<UUID>>(productsJson)
        val recipientId = singleData["recipientId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val recipient = recipientRepository.findByIdOrNull(recipientId) ?: return ServerResponse.badRequest().build()
        val requestData = DeliveryDTO.CreateDeliveryRequestDTO(
            userId = recipient.userId,
            goods = products,
            price = price,
            currencyId = currencyId,
            kazPostTrackNumber = kazPostTrackNumber,
            weight = weight,
            invoice = invoice,
            recipient = recipient
        )
        return deliveryHandler.createDelivery(requestData)
    }

    override fun updateDelivery(request: ServerRequest): ServerResponse {
        val requestData = request.body<DeliveryDTO.UpdateDeliveryRequestDTO>()
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return deliveryHandler.updateDelivery(requestData, id)
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
}