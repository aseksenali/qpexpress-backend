package kz.qpexpress.qpexpress.dto

enum class PaymentStatus(val status: String) {
    CREATING("Creating"),
    WAIT("Wait"),
    PROCESSED("Processed"),
    ERROR("Error"),
}