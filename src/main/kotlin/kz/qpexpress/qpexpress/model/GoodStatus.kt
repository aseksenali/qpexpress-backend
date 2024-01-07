package kz.qpexpress.qpexpress.model

enum class GoodStatus {
    CREATED,
    WAITING_FOR_DELIVERY,
    WAITING_FOR_PAYMENT,
    PAYED,
    DELIVERED,
    CANCELED,
    RETURNED,
    DELETED
}