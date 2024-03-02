package kz.qpexpress.qpexpress.model.jetpay

/**
 * Class for communicate with our
 */
class Gate(secret: String) {
    /**
     * kz.jetpay.sdk.SignatureHandler instance for check signature
     */
    private val signatureHandler: SignatureHandler = SignatureHandler(secret)

    /**
     * kz.jetpay.sdk.PaymentPage instance for build payment URL
     */
    private val paymentPageUrlBuilder: PaymentPage = PaymentPage(signatureHandler)

    /**
     * Method for set base payment page URL
     * @param url payment page URL
     * @return self for fluent interface
     */
    fun setBaseUrl(url: String): Gate {
        paymentPageUrlBuilder.baseUrl = url
        return this
    }

    /**
     * Method build payment URL
     * @param payment kz.jetpay.sdk.Payment instance with payment params
     * @return string URL that you can use for redirect on payment page
     */
    fun getPurchasePaymentPageUrl(payment: Payment): String {
        return paymentPageUrlBuilder.getUrl(payment)
    }

    /**
     * Method for handling callback
     * @param data raw callback data in JSON format
     * @return kz.jetpay.sdk.Callback instance
     * @throws ProcessException throws when signature is invalid
     */
    @Throws(ProcessException::class)
    fun handleCallback(data: String): Callback {
        return Callback(data, signatureHandler)
    }
}