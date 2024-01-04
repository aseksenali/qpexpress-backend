package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

fun interface IFileService {
    fun getFileById(request: ServerRequest): ServerResponse
}