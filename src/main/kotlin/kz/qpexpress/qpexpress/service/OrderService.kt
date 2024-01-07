package kz.qpexpress.qpexpress.service

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.Part
import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.dto.OrderDTO
import kz.qpexpress.qpexpress.handler.IOrderHandler
import kz.qpexpress.qpexpress.util.toFileDB
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class OrderService(
    private val orderHandler: IOrderHandler
): IOrderService {
    private fun Part.decodeToString() = inputStream.bufferedReader().use { it.readText() }
    override fun createOrder(request: ServerRequest): ServerResponse {
        val multipartData = request.multipartData()
        val singleData = multipartData.toSingleValueMap()
        val invoiceField = multipartData["invoice"]
        if (invoiceField != null) {
            val multipartRequest = request.servletRequest() as MultipartHttpServletRequest
            multipartRequest.getFile("invoice")?.toFileDB()
        }
        val documentField = multipartData["document"]
        val document = if (documentField != null) {
            val multipartRequest = request.servletRequest() as MultipartHttpServletRequest
            multipartRequest.getFile("document")?.bytes?.decodeToString()
        } else null
        if (document == null) {
            throw Exception("Document is null")
        }
        val data = jsonMapper {  }.readValue<List<GoodDTO.CreateGoodDTO>>(document)
        val userId = singleData["userId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val recipientId = singleData["recipientId"]?.decodeToString()?.toUUID() ?: return ServerResponse.badRequest().build()
        val requestData = OrderDTO.CreateOrderDTO(
            userId = userId,
            recipientId = recipientId,
            goods = data
        )
        return orderHandler.createOrder(requestData)
    }

    override fun getOrderById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return orderHandler.getOrderById(id)
    }

    override fun updateOrder(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        val data = request.body<OrderDTO.UpdateOrderDTO>()
        return orderHandler.updateOrder(id, data)
    }

    override fun deleteOrder(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return orderHandler.deleteOrder(id)
    }

    override fun getOrders(request: ServerRequest): ServerResponse {
        return orderHandler.getOrders()
    }

    override fun getMyOrders(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        return orderHandler.getMyOrders(userId)
    }

    override fun updateMyOrder(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        val orderId = request.pathVariable("orderId").toUUID() ?: return ServerResponse.badRequest().build()
        val data = request.body<OrderDTO.UpdateMyOrderDTO>()
        return orderHandler.updateMyOrder(userId, orderId, data)
    }

    override fun deleteMyOrder(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        val orderId = request.pathVariable("orderId").toUUID() ?: return ServerResponse.badRequest().build()
        return orderHandler.deleteMyOrder(userId, orderId)
    }
}