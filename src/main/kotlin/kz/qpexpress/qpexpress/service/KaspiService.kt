package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.KaspiDTO
import kz.qpexpress.qpexpress.exception.NotFoundException
import kz.qpexpress.qpexpress.handler.IKaspiHandler
import kz.qpexpress.qpexpress.util.toUUID
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
        response.onSuccess {
            logger.info("Response status: ${it.statusCode}")
            return if (it.statusCode == 0) {
                ServerResponse.ok().body(it)
            } else {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
        }.onFailure {
            when (it) {
                is NotFoundException -> {
                    logger.error("Trade points not found")
                    ServerResponse.notFound().build()
                }
                is Exception -> {
                    logger.error("Error occurred while getting trade points", it)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
                else -> {
                    logger.error("Error occurred while getting trade points")
                    ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                }
            }
        }
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
    }

    override fun registerDevice(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.RegisterDeviceRequest>()
        val response = kaspiHandler.registerDevice(data)
        response.onSuccess {
            logger.info("Response status: ${it.statusCode}")
            return if (it.statusCode == 0) {
                ServerResponse.ok().body(it)
            } else {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
        }.onFailure {
            when (it) {
                is NotFoundException -> {
                    logger.error("Device not found")
                    ServerResponse.notFound().build()
                }
                is Exception -> {
                    logger.error("Error occurred while registering device", it)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
                else -> {
                    logger.error("Error occurred while registering device")
                    ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                }
            }
        }
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
    }

    override fun createPayment(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.CreatePaymentRequest>()
        val response = kaspiHandler.createPayment(data)
        response.onSuccess {
            logger.info("Response status: ${it.statusCode}")
            return if (it.statusCode == 0) {
                ServerResponse.ok().body(it)
            } else {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
        }.onFailure {
            when (it) {
                is NotFoundException -> {
                    logger.error("Payment not found")
                    ServerResponse.notFound().build()
                }
                is Exception -> {
                    logger.error("Error occurred while creating payment", it)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
                else -> {
                    logger.error("Error occurred while creating payment")
                    ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                }
            }
        }
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
    }

    override fun createLink(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.CreateLinkRequest>()
        val response = kaspiHandler.createLink(data)
        response.onSuccess {
            logger.info("Response status: ${it.statusCode}")
            return if (it.statusCode == 0) {
                ServerResponse.ok().body(it)
            } else {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
        }.onFailure {
            when (it) {
                is NotFoundException -> {
                    logger.error("Link not found")
                    ServerResponse.notFound().build()
                }
                is Exception -> {
                    logger.error("Error occurred while creating link", it)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
                else -> {
                    logger.error("Error occurred while creating link")
                    ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                }
            }
        }
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
    }

    override fun createQR(request: ServerRequest): ServerResponse {
        val data = request.body<KaspiDTO.CreateQRCodeRequest>()
        val response = kaspiHandler.createQR(data)
        response.onSuccess {
            logger.info("Response status: ${it.statusCode}")
            return if (it.statusCode == 0) {
                ServerResponse.ok().body(it)
            } else {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
        }.onFailure {
            when (it) {
                is NotFoundException -> {
                    logger.error("QR code not found")
                    ServerResponse.notFound().build()
                }
                is Exception -> {
                    logger.error("Error occurred while creating QR code", it)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
                else -> {
                    logger.error("Error occurred while creating QR code")
                    ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                }
            }
        }
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
    }

    override fun getPaymentStatus(request: ServerRequest): ServerResponse {
        val deliveryId = request.pathVariable("deliveryId").toUUID() ?: return ServerResponse.badRequest().build()
        val status = kaspiHandler.getPaymentStatus(deliveryId)
        return ServerResponse.ok().body(status)
    }

    override fun getPaymentDetails(request: ServerRequest): ServerResponse {
        val paymentId = request.paramOrNull("paymentId")?.toLong() ?: return ServerResponse.badRequest().build()
        val response = kaspiHandler.getPaymentDetails(paymentId)
        response.onSuccess {
            logger.info("Response status: ${it.statusCode}")
            return if (it.statusCode == 0) {
                ServerResponse.ok().body(it)
            } else {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
        }.onFailure {
            when (it) {
                is NotFoundException -> {
                    logger.error("Payment details not found")
                    ServerResponse.notFound().build()
                }
                is Exception -> {
                    logger.error("Error occurred while getting payment details", it)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
                else -> {
                    logger.error("Error occurred while getting payment details")
                    ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                }
            }
        }
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
    }
}