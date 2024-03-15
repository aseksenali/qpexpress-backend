package kz.qpexpress.qpexpress.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jetpay")
data class JetpayProperties(
    val projectId: String,
    val secretKey: String,
)