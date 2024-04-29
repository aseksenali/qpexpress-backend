package kz.qpexpress.qpexpress.configuration

import kz.qpexpress.qpexpress.dto.PaymentStatus
import kz.qpexpress.qpexpress.handler.IKaspiHandler
import kz.qpexpress.qpexpress.model.GoodStatus
import kz.qpexpress.qpexpress.repository.DeliveryRepository
import kz.qpexpress.qpexpress.repository.GoodRepository
import kz.qpexpress.qpexpress.repository.KaspiPaymentRepository
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class PaymentStatusCheckJob(
    private val kaspiHandler: IKaspiHandler,
    private val kaspiPaymentRepository: KaspiPaymentRepository,
    private val deliveryRepository: DeliveryRepository,
    private val goodsRepository: GoodRepository,
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val paymentId = context.mergedJobDataMap.getLong("paymentId")
        val payment = kaspiPaymentRepository.findByPaymentId(paymentId)
        if (payment == null) {
            context.scheduler.unscheduleJob(context.trigger.key)
            return
        }
        val statusResponse = kaspiHandler.getPaymentStatus(paymentId)
        val statusCode = statusResponse.getOrNull()?.statusCode
        val data = statusResponse.getOrNull()?.data
        if (statusCode != 0) {
            updatePaymentStatus(paymentId, PaymentStatus.ERROR, context)
            context.scheduler.unscheduleJob(context.trigger.key)
        }
        if (data != null && (data.status == PaymentStatus.ERROR || data.status == PaymentStatus.PROCESSED)) {
            updatePaymentStatus(paymentId, data.status, context)
            context.scheduler.unscheduleJob(context.trigger.key)
        }
    }

    fun updatePaymentStatus(paymentId: Long, status: PaymentStatus, context: JobExecutionContext) {
        val payment = kaspiPaymentRepository.findByPaymentId(paymentId)
        if (payment == null) {
            context.scheduler.unscheduleJob(context.trigger.key)
            return
        }
        payment.status = status
        if (payment.status == PaymentStatus.PROCESSED) {
            val goods = goodsRepository.findAllByDeliveryId(payment.delivery.id!!)
            goods.forEach {
                it.status = GoodStatus.PAYED
            }
            goodsRepository.saveAll(goods)
            deliveryRepository.save(payment.delivery)
        }
        kaspiPaymentRepository.save(payment)
    }
}