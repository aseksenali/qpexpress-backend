package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.KaspiDTO
import kz.qpexpress.qpexpress.handler.IKaspiHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.*

@Service
class KaspiService(
    private val kaspiHandler: IKaspiHandler,
): IKaspiService {
    private val logger: Logger = LoggerFactory.getLogger(KaspiService::class.java)

    override fun getTradePoints(request: ServerRequest): ServerResponse {
        logger.info("Making request to Kaspi API to get trade points")
        val response = kaspiHandler.getTradePoints()
        logger.info("Response status: ${response.statusCode}")
        return if (response.statusCode == 0) {
            ServerResponse.ok().bodyWithType(response)
        } else {
            ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }

    override fun registerDevice(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.RegisterDeviceRequest>()
        val response = kaspiHandler.registerDevice(data)
        logger.info("Response status: ${response.statusCode}")
        return if (response.statusCode == 0) {
            ServerResponse.ok().bodyWithType(response)
        } else {
            ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }

    override fun createPayment(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.CreatePaymentRequest>()
        val response = kaspiHandler.createPayment(data)
        logger.info("Response status: ${response.statusCode}")
        return if (response.statusCode == 0) {
            ServerResponse.ok().bodyWithType(response)
        } else {
            ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }

    override fun createLink(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.CreateLinkRequest>()
        val response = kaspiHandler.createLink(data)
        logger.info("Response status: ${response.statusCode}")
        return if (response.statusCode == 0) {
            ServerResponse.ok().bodyWithType(response)
        } else {
            ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }

    override fun createQR(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.CreateQRCodeRequest>()
        val response = kaspiHandler.createQR(data)
        logger.info("Response status: ${response.statusCode}")
        return if (response.statusCode == 0) {
            ServerResponse.ok().bodyWithType(response)
        } else {
            ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }

    override fun getPaymentStatus(request: ServerRequest): ServerResponse {
        val paymentId = request.pathVariable("id").toLong()
        val response = kaspiHandler.getPaymentStatus(paymentId)
        logger.info("Response status: ${response.statusCode}")
        return if (response.statusCode == 0) {
            ServerResponse.ok().bodyWithType(response)
        } else {
            ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }

    override fun getPaymentDetails(request: ServerRequest): ServerResponse {
        val paymentId = request.paramOrNull("paymentId")?.toLong() ?: return ServerResponse.badRequest().build()
        val response = kaspiHandler.getPaymentDetails(paymentId)
        logger.info("Response status: ${response.statusCode}")
        return if (response.statusCode == 0) {
            ServerResponse.ok().bodyWithType(response)
        } else {
            ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }
}