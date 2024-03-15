package kz.qpexpress.qpexpress.model.jetpay

import kz.qpexpress.qpexpress.configuration.JetpayProperties
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


/**
 * Class for preparing payment params
 * Has constants like TYPE_* - possible types of payment
 * and some constants with params names
 */
@Component
@Scope("prototype")
class Payment(
    jetpayProperties: JetpayProperties
) {
    /**
     * Method return payment params
     * @return map with params
     */
    /**
     * Map with payment params
     */
    val params = mutableMapOf(PROJECT_ID to jetpayProperties.projectId, "interface_type" to INTERFACE_TYPE)


    /**
     * Setter for payment params
     * @param key name of param
     * @param o value of param
     * @return self instance for fluent interface
     */
    fun setParam(key: String, o: Any): Payment {
        if (key == BEST_BEFORE && o is ZonedDateTime) {
            params[key] = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(o)
        }

        return this
    }

    companion object {
        const val PROJECT_ID: String = "project_id"
        const val ACCOUNT_TOKEN: String = "account_token"
        const val CARD_OPERATION_TYPE: String = "card_operation_type"
        const val BEST_BEFORE: String = "best_before"
        const val CLOSE_ON_MISSCLICK: String = "close_on_missclick"
        const val CSS_MODAL_WRAP: String = "css_modal_wrap"
        const val CUSTOMER_ID: String = "customer_id"
        const val FORCE_ACS_NEW_WINDOW: String = "force_acs_new_window"
        const val FORCE_PAYMENT_METHOD: String = "force_payment_method"
        const val LANGUAGE_CODE: String = "language_code"
        const val LIST_PAYMENT_BLOCK: String = "list_payment_block"
        const val MERCHANT_FAIL_URL: String = "merchant_fail_url"
        const val MERCHANT_SUCCESS_URL: String = "merchant_success_url"
        const val MODE: String = "mode"
        const val PAYMENT_AMOUNT: String = "payment_amount"
        const val PAYMENT_CURRENCY: String = "payment_currency"
        const val PAYMENT_DESCRIPTION: String = "payment_description"
        const val PAYMENT_ID: String = "payment_id"
        const val RECURRING_REGISTER: String = "recurring_register"
        const val CUSTOMER_FIRST_NAME: String = "customer_first_name"
        const val CUSTOMER_LAST_NAME: String = "customer_last_name"
        const val CUSTOMER_PHONE: String = "customer_phone"
        const val CUSTOMER_EMAIL: String = "customer_email"
        const val CUSTOMER_COUNTRY: String = "customer_country"
        const val CUSTOMER_STATE: String = "customer_state"
        const val CUSTOMER_CITY: String = "customer_city"
        const val CUSTOMER_DAY_OF_BIRTH: String = "customer_day_of_birth"
        const val CUSTOMER_SSN: String = "customer_ssn"
        const val BILLING_POSTAL: String = "billing_postal"
        const val BILLING_COUNTRY: String = "billing_country"
        const val BILLING_REGION: String = "billing_region"
        const val BILLING_CITY: String = "billing_city"
        const val BILLING_ADDRESS: String = "billing_address"
        const val REDIRECT: String = "redirect"
        const val REDIRECT_FAIL_MODE: String = "redirect_fail_mode"
        const val REDIRECT_FAIL_URL: String = "redirect_fail_url"
        const val REDIRECT_ON_MOBILE: String = "redirect_on_mobile"
        const val REDIRECT_SUCCESS_MODE: String = "redirect_success_mode"
        const val REDIRECT_SUCCESS_URL: String = "redirect_success_url"
        const val REDIRECT_TOKENIZE_MODE: String = "redirect_tokenize_mode"
        const val REDIRECT_TOKENIZE_URL: String = "redirect_tokenize_url"
        const val REGION_CODE: String = "region_code"
        const val TARGET_ELEMENT: String = "target_element"
        const val TERMINAL_ID: String = "terminal_id"
        const val BASEURL: String = "baseurl"
        const val PAYMENT_EXTRA_PARAM: String = "payment_extra_param"

        const val TYPE_PURCHASE: String = "purchase"
        const val TYPE_PAYOUT: String = "payout"
        const val TYPE_RECURRING: String = "recurring"

        private const val INTERFACE_TYPE = "{\"id\": 21}"
    }
}