package kz.qpexpress.qpexpress.service

import jakarta.servlet.http.Part
import kz.qpexpress.qpexpress.dto.DeliveryDTO
import kz.qpexpress.qpexpress.handler.DeliveryHandler
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.util.toUUID
import org.hibernate.engine.jdbc.BlobProxy
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.util.*

@Service
class DeliveryService(
    private val orderHandler: DeliveryHandler
) : IDeliveryService {
    override fun getAllDeliveries(request: ServerRequest): ServerResponse {
        return orderHandler.getAllDeliveries()
    }

    override fun getDeliveryById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return orderHandler.getDeliveryById(id)
    }

    override fun createDelivery(request: ServerRequest): ServerResponse {
        val multipartData = request.multipartData()
        val singleData = multipartData.toSingleValueMap()
        val invoiceField = multipartData["invoice"]
        val invoice = if (invoiceField != null) {
            val multipartRequest = request.servletRequest() as MultipartHttpServletRequest
            val file = multipartRequest.getFile("invoice")
            if (file == null) null
            else {
                val fileName = file.originalFilename ?: UUID.randomUUID().toString()
                val contentType = file.contentType
                val invoiceData = BlobProxy.generateProxy(file.inputStream, file.size)
                FileDB().also {
                    it.name = fileName
                    it.contentType = contentType.toString()
                    it.data = invoiceData
                }
            }
        } else null
        val userId = singleData["userId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val orderId = singleData["orderId"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val trackingNumber =
            singleData["trackingNumber"]?.decodeToString()
        val productLink = singleData["productLink"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val name = singleData["name"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val description = singleData["description"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val price = singleData["price"]?.decodeToString()?.toFloatOrNull()
        val currencyId =
            singleData["currencyId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val countryId =
            singleData["countryId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val weight = singleData["weight"]?.decodeToString()?.toFloatOrNull()
        val originalBox =
            singleData["originalBox"]?.decodeToString()?.toBoolean() ?: return ServerResponse.badRequest().build()
        val requestData = DeliveryDTO.CreateDeliveryRequestDTO(
            userId = userId,
            orderId = orderId,
            trackingNumber = trackingNumber,
            productLink = productLink,
            name = name,
            description = description,
            price = price,
            currencyId = currencyId,
            invoice = invoice,
            countryId = countryId,
            weight = weight,
            originalBox = originalBox,
        )
        return orderHandler.createDelivery(requestData)
    }

    fun Part.decodeToString() = inputStream.bufferedReader().use { it.readText() }

    override fun updateDelivery(request: ServerRequest): ServerResponse {
        val requestData = request.body<DeliveryDTO.UpdateDeliveryRequestDTO>()
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return orderHandler.updateDelivery(requestData, id)
    }

    override fun deleteDelivery(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return orderHandler.deleteDelivery(id)
    }

    override fun getMyDeliveries(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        return orderHandler.getMyDeliveries(userId)
    }

    override fun getMyDeliveryById(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        val orderId = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return orderHandler.getMyDeliveryById(userId, orderId)
    }
}