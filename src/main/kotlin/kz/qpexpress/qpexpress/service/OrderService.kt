package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.OrderDTO
import kz.qpexpress.qpexpress.handler.IOrderHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class OrderService(
    private val orderHandler: IOrderHandler
): IOrderService {
    override fun createOrder(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.UNAUTHORIZED
        ).build()
        val data = request.body<OrderDTO.CreateOrderDTO>()
        return orderHandler.createOrder(userId, data)
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