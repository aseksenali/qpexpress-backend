package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IFileService {
    fun getFileById(request: ServerRequest): ServerResponse
    fun downloadFileById(request: ServerRequest): ServerResponse
    fun uploadFile(request: ServerRequest): ServerResponse
}