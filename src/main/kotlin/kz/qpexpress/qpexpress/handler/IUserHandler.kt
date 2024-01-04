package kz.qpexpress.qpexpress.handler

import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IUserHandler {
    fun getMyInformation(id: UUID): ServerResponse
    fun getAll(): ServerResponse
}