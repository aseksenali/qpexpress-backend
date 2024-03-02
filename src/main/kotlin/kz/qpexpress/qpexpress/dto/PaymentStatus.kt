package kz.qpexpress.qpexpress.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class PaymentStatus(val status: String) {
    CREATING("Creating"),
    QR_TOKEN_CREATED("QrTokenCreated"),
    WAIT("Wait"),
    PROCESSED("Processed"),
    ERROR("Error");

    @JsonValue
    fun toValue(): String = this.status

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(value: String): PaymentStatus {
            for (enumValue in values()) {
                if (enumValue.status.equals(value, ignoreCase = true)) {
                    return enumValue
                }
            }
            throw IllegalArgumentException("Unknown enum string value: $value")
        }
    }
}