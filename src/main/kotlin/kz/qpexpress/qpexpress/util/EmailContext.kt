package kz.qpexpress.qpexpress.util

sealed interface EmailContext {
    val from: String
    val to: String
    val subject: String
    val email: String
    val attachment: String
    val fromDisplayName: String
    val displayName: String
    val templateLocation: String

    data class EmailConfirmationContext(
        override val from: String,
        override val to: String,
        override val subject: String,
        override val email: String,
        override val attachment: String,
        override val fromDisplayName: String,
        override val displayName: String,
        override val templateLocation: String = "email/email-confirmation",
    ) : EmailContext
}