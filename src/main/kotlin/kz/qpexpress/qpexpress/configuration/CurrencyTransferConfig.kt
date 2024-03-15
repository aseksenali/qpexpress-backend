package kz.qpexpress.qpexpress.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class CurrencyTransferConfig {
    @Bean("currencyRestTemplate")
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate =
        restTemplateBuilder.rootUri("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies").build()
}