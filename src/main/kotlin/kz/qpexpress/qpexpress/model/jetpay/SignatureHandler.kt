package kz.qpexpress.qpexpress.model.jetpay

import kz.qpexpress.qpexpress.configuration.JetpayProperties
import org.springframework.stereotype.Component
import java.security.GeneralSecurityException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * Class for make/check signature
 */
@Component
class SignatureHandler(
    private val jetpayProperties: JetpayProperties
) {

    /**
     * Need sort params before sign or not need
     */
    private var sortParams = true

    /**
     * Method for check signature
     * @param sign signature for check
     * @param params parameters with which signature was obtained
     * @return that signature valid or not valid
     */
    fun check(sign: String, params: Map<String, Any>): Boolean {
        return sign == sign(params)
    }

    /**
     * Method for make signature
     * @param params parameters with which signature will be obtained
     * @return signature
     */
    fun sign(params: Map<String, Any>): String {
        val paramsToSign = getParamsToSign(params = params, ignore = IGNORE_KEYS)
        val paramsListToSign = paramsToSign.values.toList()

        if (sortParams) {
            paramsListToSign.sortedBy { it }
        }

        val paramsStringToSign = paramsListToSign.joinToString(DELIMITER_PARAM)

        try {
            val shaHMAC = Mac.getInstance(ALGORITHM)
            val secretKey = SecretKeySpec(jetpayProperties.secretKey.toByteArray(), ALGORITHM)
            shaHMAC.init(secretKey)
            val hash = Base64.getEncoder().encodeToString(shaHMAC.doFinal(paramsStringToSign.toByteArray()))
            return hash
        } catch (exception: GeneralSecurityException) {
            throw RuntimeException(exception)
        }
    }

    /**
     * Method for preparing params
     * @param params map with params
     * @param prefix add before key
     * @param ignore ignore specific keys
     * @return prepared map with params
     */
    private fun getParamsToSign(
        params: Map<*, *>,
        ignore: Array<String> = arrayOf(),
        prefix: String = ""
    ): Map<String, String> {
        return params.entries
            .filter { param -> ignore.any { it == param.key } }
            .flatMap { param ->
                val key = prefix + (if (prefix == "") "" else DELIMITER_KEY) + param.key
                when (val value = param.value) {
                    is Boolean -> {
                        listOf(key to key + DELIMITER_KEY + if (value) "1" else "0")
                    }

                    is List<*> -> {
                        value.mapIndexed { index, v ->
                            index.toString() to v as String
                        }
                    }

                    is Map<*, *> -> {
                        getParamsToSign(params = value, prefix = key, ignore = ignore).entries.map { entry ->
                            entry.key to entry.value
                        }
                    }

                    else -> {
                        listOf(key to key + DELIMITER_KEY + value.toString())
                    }
                }
            }
            .toMap()
    }

    companion object {
        /**
         * Delimiter for separate key and value
         */
        private const val DELIMITER_KEY = ':'

        /**
         * Delimiter for separate params
         */
        private const val DELIMITER_PARAM = ";"

        /**
         * Ignore keys from signature
         */
        private val IGNORE_KEYS = arrayOf("frame_mode")

        /**
         * Crypto algorithm
         */
        private val ALGORITHM = "HmacSHA512"
    }
}