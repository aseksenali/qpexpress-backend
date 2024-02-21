package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IKaspiService {
    fun getTradePoints(request: ServerRequest): ServerResponse
    fun registerDevice(request: ServerRequest): ServerResponse
    fun createPayment(request: ServerRequest): ServerResponse
    fun createLink(request: ServerRequest): ServerResponse
    fun createQR(request: ServerRequest): ServerResponse
    fun getPaymentStatus(request: ServerRequest): ServerResponse
    fun getPaymentDetails(request: ServerRequest): ServerResponse
}