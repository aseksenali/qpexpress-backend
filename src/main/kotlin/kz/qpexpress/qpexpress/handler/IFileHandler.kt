package kz.qpexpress.qpexpress.handler

import org.springframework.http.MediaType
import java.util.*

fun interface IFileHandler {
    fun getFileById(fileId: UUID): Pair<ByteArray, Pair<MediaType, String>>
}