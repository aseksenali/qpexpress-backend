package kz.qpexpress.qpexpress.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ssl.SslBundle
import org.springframework.boot.ssl.SslStoreBundle
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.client.RestTemplate
import java.security.KeyStore
import java.security.MessageDigest
import java.security.cert.Certificate

@Configuration
class KaspiConfig {
    private final val logger: Logger = LoggerFactory.getLogger(KaspiConfig::class.java)
    @Bean(name = ["kaspiRestTemplate"])
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        val keyStore = KeyStore.getInstance("PKCS12")
        val keyStoreResource = ClassPathResource("/certs/certfile.pfx")
        val keyStorePassword = "o1qw}\"2;//@hleow0#:401MdVU2]7J+F!OvU>fFS\$|.GZ\\I_Z|"
        keyStore.load(keyStoreResource.inputStream, keyStorePassword.toCharArray())
        logCertificateFingerprint(keyStore, "1")
        val sslBundle = SslBundle.of(SslStoreBundle.of(keyStore, keyStorePassword, null))
        return restTemplateBuilder
            .setSslBundle(sslBundle)
            .interceptors(LoggingRequestInterceptor())
            .defaultHeader("Content-Type", "application/json")
            .rootUri("https://qrapi-cert-ip.kaspi.kz").build()
    }

    private fun logCertificateFingerprint(keyStore: KeyStore, alias: String) {
        val cert: Certificate = keyStore.getCertificate(alias)
        val sha1: MessageDigest = MessageDigest.getInstance("SHA1")
        val sha1Fingerprint: ByteArray = sha1.digest(cert.encoded)
        val fingerprint: String = sha1Fingerprint.joinToString(":") { String.format("%02X", it) }
        logger.info("SHA1 Fingerprint: $fingerprint")
    }
}