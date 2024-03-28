package kz.qpexpress.qpexpress.model.jetpay

import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


/**
 * Class for build payment URL
 */
@Component
class PaymentPage(
    private val signatureHandler: SignatureHandler,
) {
    var baseUrl: String = "https://paymentpage.jetpay.kz/payment"
    private final val charSet: String = "UTF-8"

    /**
     * Method build payment URL
     * @param payment kz.jetpay.sdk.Payment instance with payment params
     * @return string URL that you can use for redirect on payment page
     */
    fun getUrl(payment: Payment): String {
        val signature = "&signature=" + encode(signatureHandler.sign(payment.params))
        val query = payment.params.entries.map { e ->
            e.key + "=" + encode(
                e.value
            )
        }.joinToString("&")

        return "$baseUrl?$query$signature"
    }

    /**
     * Method for URL encoding payment params
     * @param param payment param value
     * @return URL encoded param
     */
    private fun encode(param: Any): String {
        try {
            return URLEncoder.encode(param.toString(), charSet)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }
    }
}