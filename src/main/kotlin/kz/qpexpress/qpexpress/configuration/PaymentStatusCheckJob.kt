package kz.qpexpress.qpexpress.configuration

import kz.qpexpress.qpexpress.dto.PaymentStatus
import kz.qpexpress.qpexpress.handler.IKaspiHandler
import kz.qpexpress.qpexpress.repository.KaspiPaymentRepository
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class PaymentStatusCheckJob(
    private val kaspiHandler: IKaspiHandler,
    private val kaspiPaymentRepository: KaspiPaymentRepository,
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val paymentId = context.mergedJobDataMap.getLong("paymentId")
        val statusResponse = kaspiHandler.getPaymentStatus(paymentId)
        val status = statusResponse.data.status
        if (status == PaymentStatus.ERROR || status == PaymentStatus.PROCESSED) {
            updatePaymentStatus(paymentId, status, context)
            context.scheduler.unscheduleJob(context.trigger.key)
        }
    }

    // Замените этот метод на реальный метод для работы с вашей базой данных
    fun updatePaymentStatus(paymentId: Long, status: PaymentStatus, context: JobExecutionContext) {
        val payment = kaspiPaymentRepository.findByPaymentId(paymentId)
        if (payment == null) {
            // Обработка ошибки
            context.scheduler.unscheduleJob(context.trigger.key)
            return
        }
        payment.status = status
        kaspiPaymentRepository.save(payment)
    }
}