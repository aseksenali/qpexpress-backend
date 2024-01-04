package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.ReceiverDTO
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

fun interface IReceiverHandler {
    fun createReceiver(data: ReceiverDTO.CreateReceiverRequestDTO, userId: UUID): ServerResponse
}