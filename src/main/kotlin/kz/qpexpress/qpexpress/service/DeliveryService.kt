package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.DeliveryDTO
import kz.qpexpress.qpexpress.handler.DeliveryHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

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
        val data = request.body<DeliveryDTO.CreateDeliveryRequestDTO>()
        return orderHandler.createDelivery(data)
    }

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