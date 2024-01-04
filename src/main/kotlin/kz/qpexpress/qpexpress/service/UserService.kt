package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.handler.IUserHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Service
class UserService(
    private val userService: IUserHandler
): IUserService {
    override fun getMyInformation(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID()
            ?: return ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
        return userService.getMyInformation(userId)
    }

    override fun getAll(request: ServerRequest): ServerResponse {
        return userService.getAll()
    }
}