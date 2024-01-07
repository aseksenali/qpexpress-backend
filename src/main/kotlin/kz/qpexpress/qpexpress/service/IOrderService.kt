package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IOrderService {
    fun createOrder(request: ServerRequest): ServerResponse
    fun getOrderById(request: ServerRequest): ServerResponse
    fun updateOrder(request: ServerRequest): ServerResponse
    fun deleteOrder(request: ServerRequest): ServerResponse
    fun getOrders(request: ServerRequest): ServerResponse
    fun getMyOrders(request: ServerRequest): ServerResponse
    fun updateMyOrder(request: ServerRequest): ServerResponse
    fun deleteMyOrder(request: ServerRequest): ServerResponse
}