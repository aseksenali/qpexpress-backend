package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.configuration.JetpayProperties
import kz.qpexpress.qpexpress.dto.JetpayDTO
import kz.qpexpress.qpexpress.dto.PaymentStatus
import kz.qpexpress.qpexpress.exception.NotFoundException
import kz.qpexpress.qpexpress.model.GoodStatus
import kz.qpexpress.qpexpress.model.JetpayPayment
import kz.qpexpress.qpexpress.model.jetpay.Gate
import kz.qpexpress.qpexpress.model.jetpay.Payment
import kz.qpexpress.qpexpress.repository.DeliveryRepository
import kz.qpexpress.qpexpress.repository.GoodRepository
import kz.qpexpress.qpexpress.repository.JetpayPaymentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Service
class JetpayHandler(
    private val gate: Gate,
    private val jetpayProperties: JetpayProperties,
    private val paymentRepository: JetpayPaymentRepository,
    private val deliveryRepository: DeliveryRepository,
    private val goodsRepository: GoodRepository,
): IJetpayHandler {
    private final val logger: Logger = LoggerFactory.getLogger(JetpayHandler::class.java)

    override fun createPayment(data: JetpayDTO.CreatePaymentRequest): Result<URI> {
        val delivery = deliveryRepository.findByIdOrNull(data.deliveryId) ?: return Result.failure(NotFoundException)
        val latestPayment = paymentRepository.findFirstByOrderByPaymentIdDesc()
        val paymentId = latestPayment?.paymentId?.plus(1) ?: 1
        val entity = JetpayPayment().also {
            it.paymentId = paymentId
            it.totalAmount = data.amount
            it.date = LocalDateTime.now()
            it.status = PaymentStatus.CREATING
            it.delivery = delivery
        }
        val savedEntity = paymentRepository.save(entity)

        val payment = Payment(jetpayProperties.projectId)
            .setParam(Payment.PAYMENT_ID, savedEntity.paymentId)
            .setParam(Payment.CUSTOMER_ID, savedEntity.createdBy)
            .setParam(Payment.PAYMENT_AMOUNT, (data.amount * 100).toUInt())
            .setParam(Payment.PAYMENT_CURRENCY, "KZT")
            .setParam(Payment.PAYMENT_DESCRIPTION, "Оплата заказа №${entity.paymentId}")
        val uri = URI.create(gate.getPurchasePaymentPageUrl(payment))
        return Result.success(uri)
    }

    override fun getPaymentStatus(deliveryId: UUID): Result<PaymentStatus> {
        val payment = paymentRepository.findAllByDeliveryId(deliveryId)
        if (payment.isEmpty()) {
            return Result.failure(NotFoundException)
        }
        if (payment.any { it.status == PaymentStatus.PROCESSED }) {
            return Result.success(PaymentStatus.PROCESSED)
        }
        if (payment.all { it.status == PaymentStatus.ERROR }) {
            return Result.success(PaymentStatus.ERROR)
        }
        return Result.success(PaymentStatus.WAIT)
    }

    override fun updatePaymentStatus(data: JetpayDTO.UpdatePaymentStatusRequest): Result<PaymentStatus> {
        logger.info("Update payment status: $data")
        val payment = paymentRepository.findByPaymentId(data.payment.id.toLong()) ?: return Result.failure(NotFoundException)
        if (data.payment.status == "success") {
            payment.status = PaymentStatus.PROCESSED
            val goods = goodsRepository.findAllByDeliveryId(payment.delivery.id!!)
            goods.forEach {
                it.status = GoodStatus.PAYED
            }
            goodsRepository.saveAll(goods)
            paymentRepository.save(payment)
        } else {
            payment.status = PaymentStatus.ERROR
            paymentRepository.save(payment)
        }
        return Result.success(PaymentStatus.PROCESSED)
    }
}