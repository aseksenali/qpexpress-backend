package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IGoodService {
    fun createGood(request: ServerRequest): ServerResponse
    fun getGoodsByDeliveryId(request: ServerRequest): ServerResponse
    fun getGoodsByOrderId(request: ServerRequest): ServerResponse
    fun getGoodsByUserId(request: ServerRequest): ServerResponse
    fun getGoodsByUserIdAndStatus(request: ServerRequest): ServerResponse
}