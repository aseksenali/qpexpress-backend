package kz.qpexpress.qpexpress.configuration

import org.quartz.*
import org.springframework.stereotype.Component

@Component
class PaymentStatusChecker(
    private val scheduler: Scheduler
) {
    fun startChecking(paymentId: Int, interval: Long) {
        val jobDetail = JobBuilder.newJob(PaymentStatusCheckJob::class.java)
            .withIdentity("job$paymentId", "group1")
            .usingJobData("paymentId", paymentId)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("trigger$paymentId", "group1")
            .startNow()
            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(interval.toInt())
                .repeatForever())
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

    fun stopChecking(paymentId: Int) {
        scheduler.unscheduleJob(TriggerKey.triggerKey("trigger$paymentId", "group1"))
    }
}