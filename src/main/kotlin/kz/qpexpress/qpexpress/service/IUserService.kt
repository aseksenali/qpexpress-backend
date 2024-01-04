package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
interface IUserService {
    fun getMyInformation(request: ServerRequest): ServerResponse
    fun getAll(request: ServerRequest): ServerResponse
}