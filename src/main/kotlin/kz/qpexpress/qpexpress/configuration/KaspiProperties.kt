package kz.qpexpress.qpexpress.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kaspi")
data class KaspiProperties(
    val organizationBin: String,
    val deviceToken: String,
)