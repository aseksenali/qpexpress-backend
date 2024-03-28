package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IDeliveryService {
    fun getAllDeliveries(request: ServerRequest): ServerResponse
    fun getDeliveryById(request: ServerRequest): ServerResponse
    fun createDelivery(request: ServerRequest): ServerResponse
    fun updateDelivery(request: ServerRequest): ServerResponse
    fun updateDeliveryStatus(request: ServerRequest): ServerResponse
    fun deleteDelivery(request: ServerRequest): ServerResponse
    fun getMyDeliveries(request: ServerRequest): ServerResponse
    fun getMyDeliveryById(request: ServerRequest): ServerResponse
}