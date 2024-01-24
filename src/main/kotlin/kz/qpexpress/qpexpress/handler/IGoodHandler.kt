package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.GoodDTO
import kz.qpexpress.qpexpress.model.GoodStatus
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IGoodHandler {
    fun createGood(goodDTO: GoodDTO.CreateGoodDTO, orderId: UUID): ServerResponse
    fun getGoods(userId: UUID?, recipientId: UUID?, orderId: UUID?, deliveryId: UUID?, status: GoodStatus?): ServerResponse
}