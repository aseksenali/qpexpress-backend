package kz.qpexpress.qpexpress.model.jetpay

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

/**
 * Class for processing our callbacks
 * Has constants like STATUS_* - possible statuses of payment which we got
 */
class Callback(
    callbackData: String,
    private val signatureHandler: SignatureHandler
) {
    /**
     * Decoded callback data
     */
    private var data: MutableMap<String, Any>? = null

    /**
     * kz.jetpay.sdk.Callback signature
     */
    @get:Throws(ProcessException::class)
    var signature: String? = null
        /**
         * @return callback signature
         * @throws ProcessException throws when can't decode
         */
        get() {
            if (field != null) {
                return field
            }

            arrayOf(
                "signature",
                "general.signature",
            ).forEach { signPath ->
                val sign = getValue(signPath)

                if (sign != null) {
                    return sign.toString().also { field = it }
                }
            }

            throw ProcessException("Undefined signature")
        }
        private set

    /**
     * kz.jetpay.sdk.Callback constructor decode callback data and check signature
     * @param callbackData raw callback data
     * @param signHandler signatureHandler for check callback signature
     * @throws ProcessException throws when signature is incorrect
     */
    init {
        decodeResponse(callbackData)

        if (!checkSignature()) {
            throw ProcessException("Invalid signature")
        }
    }

    val payment: Map<*, *>?
        /**
         * @return Map with payment data
         */
        get() {
            val payment = getValue("payment")

            return if (payment != null) payment as HashMap<*, *>? else null
        }

    val paymentStatus: String?
        /**
         * @return payment status
         */
        get() {
            val paymentStatus = getValue("payment.status")

            return paymentStatus?.toString()
        }

    val paymentId: String?
        /**
         * @return payment id
         */
        get() {
            val paymentId = getValue("payment.id")

            return paymentId?.toString()
        }

    /**
     * @return that signature valid or not valid
     * @throws ProcessException
     */
    @Throws(ProcessException::class)
    fun checkSignature(): Boolean {
        val signature = signature!!
        if (data == null) {
            throw ProcessException("Undefined data")
        } else {
            removeParam("signature", data!!)
            return signatureHandler.check(signature, data!!)
        }
    }

    /**
     * Method for get value in multilevel map by key
     * @param path key for searching
     * @return mixed value or null
     */
    fun getValue(path: String): Any? {
        val keysPath = path.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var cbData = this.data
        var value: Any? = null

        for (key in keysPath) {
            value = cbData!![key]

            if (value == null) {
                break
            }

            if (value is Map<*, *>) {
                cbData = value as HashMap<String, Any>?
            }
        }

        return value
    }

    /**
     * Method for remove value in multilevel map
     * @param name key for searching
     * @param data map with callback data
     */
    private fun removeParam(name: String, data: MutableMap<String, Any>) {
        val value = data[name]

        if (value != null) {
            data -= name
            return
        }

        for ((_, entryValue) in data) {
            if (entryValue is MutableMap<*, *>) {
                removeParam(name, entryValue as MutableMap<String, Any>)
            }
        }
    }

    /**
     * Method for decode callback data
     * @param callbackData raw data in JSON format
     * @throws ProcessException throws when can't decode
     */
    @Throws(ProcessException::class)
    private fun decodeResponse(callbackData: String) {
        val mapper = ObjectMapper()

        try {
            data = mapper.readValue(
                callbackData,
                object : TypeReference<MutableMap<String, Any>>() {}
            ) as HashMap<String, Any>?
        } catch (e: IOException) {
            throw ProcessException(e)
        }
    }

    companion object {
        const val STATUS_SUCCESS: String = "success"
        const val STATUS_DECLINE: String = "decline"
        const val STATUS_AW_3DS: String = "awaiting 3ds result"
        const val STATUS_AW_REDIRECT: String = "awaiting redirect result"
        const val STATUS_AW_CUSTOMER: String = "awaiting customer"
        const val STATUS_AW_CLARIFICATION: String = "awaiting clarification"
        const val STATUS_AW_CAPTURE: String = "awaiting capture"
        const val STATUS_CANCELLED: String = "cancelled"
        const val STATUS_REFUNDED: String = "refunded"
        const val STATUS_PARTIALLY_REFUNDED: String = "partially refunded"
        const val STATUS_PROCESSING: String = "processing"
        const val STATUS_ERROR: String = "error"
        const val STATUS_REVERSED: String = "reversed"
    }
}