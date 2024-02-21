package kz.qpexpress.qpexpress.configuration

import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.slf4j.LoggerFactory
import org.springframework.util.StreamUtils
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class LoggingRequestInterceptor : ClientHttpRequestInterceptor {
    private val logger = LoggerFactory.getLogger(LoggingRequestInterceptor::class.java)

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        logger.info("Request URI: ${request.uri}")
        logger.info("Request Method: ${request.method}")
        logger.info("Request Headers: ${request.headers}")
        logger.info("Request Body: ${String(body, StandardCharsets.UTF_8)}")
        val response = execution.execute(request, body)
        if (response.statusCode.is2xxSuccessful) {
            val responseBody = StreamUtils.copyToString(response.body, Charset.defaultCharset())
            println("Response: $responseBody")
        }
        return response
    }
}