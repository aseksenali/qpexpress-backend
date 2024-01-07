package kz.qpexpress.qpexpress.service

import jakarta.servlet.http.Part
import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.handler.IGoodHandler
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.model.GoodStatus
import kz.qpexpress.qpexpress.util.toUUID
import org.hibernate.engine.jdbc.BlobProxy
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class GoodService(
    private val goodHandler: IGoodHandler
) : IGoodService {
    private fun Part.decodeToString() = inputStream.bufferedReader().use { it.readText() }

    override fun createGood(request: ServerRequest): ServerResponse {
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
        val orderId = request.pathVariable("orderId").toUUID() ?: return ServerResponse.badRequest().build()
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        val trackingNumber =
            singleData["trackingNumber"]?.decodeToString()
        val name = singleData["name"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val description = singleData["description"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val customOrderId = singleData["customOrderId"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val price = singleData["price"]?.decodeToString()?.toFloat() ?: return ServerResponse.badRequest().build()
        val link = singleData["link"]?.decodeToString() ?: return ServerResponse.badRequest().build()
        val currencyId =
            singleData["currencyId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val countryId =
            singleData["countryId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val originalBox =
            singleData["originalBox"]?.decodeToString()?.toBoolean() ?: return ServerResponse.badRequest().build()
        val quantity = singleData["quantity"]?.decodeToString()?.toInt() ?: 1
        val requestData = GoodDTO.CreateGoodDTO(
            trackingNumber = trackingNumber,
            name = name,
            description = description,
            price = price,
            currencyId = currencyId,
            invoice = invoice,
            countryId = countryId,
            originalBox = originalBox,
            link = link,
            customOrderId = customOrderId,
            quantity = quantity,
            userId = userId,
            orderId = orderId
        )
        return goodHandler.createGood(requestData)
    }

    override fun getGoodsByDeliveryId(request: ServerRequest): ServerResponse {
        val deliveryId = request.pathVariable("deliveryId").toUUID() ?: return ServerResponse.badRequest().build()
        return goodHandler.getGoodsByDeliveryId(deliveryId)
    }

    override fun getGoodsByOrderId(request: ServerRequest): ServerResponse {
        val orderId = request.pathVariable("orderId").toUUID() ?: return ServerResponse.badRequest().build()
        return goodHandler.getGoodsByOrderId(orderId)
    }

    override fun getGoodsByUserId(request: ServerRequest): ServerResponse {
        val userId = request.pathVariable("userId").toUUID() ?: return ServerResponse.badRequest().build()
        return goodHandler.getGoodsByUserId(userId)
    }

    override fun getGoodsByUserIdAndStatus(request: ServerRequest): ServerResponse {
        val userId = request.pathVariable("userId").toUUID() ?: return ServerResponse.badRequest().build()
        val status = GoodStatus.valueOf(request.pathVariable("status"))
        return goodHandler.getGoodsByUserIdAndStatus(userId, status)
    }
}