package kz.qpexpress.qpexpress.repository

import java.util.UUID

interface RecipientIdAndStatus {
    val id: UUID
    val status: String
}