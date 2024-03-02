package kz.qpexpress.qpexpress.model.jetpay

/**
 * Exception that throws when has some errors in payment flow
 */
class ProcessException : Exception {
    /**
     * Can be with exception
     * @param exception exception
     */
    constructor(exception: Throwable?) : super(exception)

    /**
     * Can be with message
     * @param message exception message
     */
    constructor(message: String?) : super(message)
}