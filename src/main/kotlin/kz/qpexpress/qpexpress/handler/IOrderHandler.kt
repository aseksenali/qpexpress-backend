package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.OrderDTO
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IOrderHandler {
    fun createOrder(userId: UUID, data: OrderDTO.CreateOrderDTO): ServerResponse
    fun getOrderById(id: UUID): ServerResponse
    fun updateOrder(id: UUID, data: OrderDTO.UpdateOrderDTO): ServerResponse
    fun deleteOrder(id: UUID): ServerResponse
    fun getOrders(): ServerResponse
    fun getMyOrders(userId: UUID): ServerResponse
    fun updateMyOrder(id: UUID, userId: UUID, data: OrderDTO.UpdateMyOrderDTO): ServerResponse
    fun deleteMyOrder(id: UUID, userId: UUID): ServerResponse
}