package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.model.GoodStatus
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IGoodHandler {
    fun createGood(goodDTO: GoodDTO.CreateGoodDTO): ServerResponse
    fun getGoodsByDeliveryId(deliveryId: UUID): ServerResponse
    fun getGoodsByOrderId(orderId: UUID): ServerResponse
    fun getGoodsByUserId(userId: UUID): ServerResponse
    fun getGoodsByUserIdAndStatus(userId: UUID, status: GoodStatus): ServerResponse
}