package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.DeliveryDTO
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IDeliveryHandler {
    fun createDelivery(data: DeliveryDTO.CreateDeliveryRequestDTO): ServerResponse
    fun updateDelivery(data: DeliveryDTO.UpdateDeliveryRequestDTO, id: UUID): ServerResponse
    fun deleteDelivery(id: UUID): ServerResponse
    fun getAllDeliveries(): ServerResponse
    fun getDeliveryById(id: UUID): ServerResponse

    fun getMyDeliveries(userId: UUID): ServerResponse
    fun getMyDeliveryById(userId: UUID, deliveryId: UUID): ServerResponse
}