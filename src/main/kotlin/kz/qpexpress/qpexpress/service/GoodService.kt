package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.handler.IGoodHandler
import kz.qpexpress.qpexpress.model.GoodStatus
import kz.qpexpress.qpexpress.repository.FileDBRepository
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull

@Service
class GoodService(
    private val goodHandler: IGoodHandler, private val fileDBRepository: FileDBRepository
) : IGoodService {
    override fun createGood(request: ServerRequest): ServerResponse {
        val data = request.body<GoodDTO.CreateGoodDTO>()
        val orderId = request.paramOrNull("id")?.toUUID() ?: return ServerResponse.badRequest().build()
        return goodHandler.createGood(data, orderId)
    }

    override fun getGoods(request: ServerRequest): ServerResponse {
        val userId = request.paramOrNull("userId")?.toUUID()
        val orderId = request.paramOrNull("orderId")?.toUUID()
        val deliveryId = request.paramOrNull("deliveryId")?.toUUID()
        val status = request.paramOrNull("status")?.let { GoodStatus.valueOf(it) }
        val recipientId = request.paramOrNull("recipientId")?.toUUID()
        return goodHandler.getGoods(userId = userId, orderId = orderId, deliveryId = deliveryId, status = status, recipientId = recipientId)
    }
}