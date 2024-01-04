package kz.qpexpress.qpexpress

import kz.qpexpress.qpexpress.configuration.KeycloakProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
@EnableTransactionManagement
@EnableConfigurationProperties(KeycloakProperties::class)
class QpExpressApplication

fun main(args: Array<String>) {
    runApplication<QpExpressApplication>(*args)
}