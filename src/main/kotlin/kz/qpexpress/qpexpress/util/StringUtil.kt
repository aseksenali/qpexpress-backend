package kz.qpexpress.qpexpress.util

import java.util.*

fun String.toUUID(): UUID? {
    return try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}