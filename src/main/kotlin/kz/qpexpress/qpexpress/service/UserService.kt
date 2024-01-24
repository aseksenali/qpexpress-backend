package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.UserDTO
import kz.qpexpress.qpexpress.handler.IUserHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class UserService(
    private val userHandler: IUserHandler
): IUserService {
    override fun getMyInformation(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID()
            ?: return ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
        return userHandler.getUserInformation(userId)
    }

    override fun getAll(request: ServerRequest): ServerResponse {
        return userHandler.getAll()
    }

    override fun getUserById(request: ServerRequest): ServerResponse {
        val userId = request.pathVariable("id").toUUID()
            ?: return ServerResponse.status(HttpStatus.BAD_REQUEST).build()
        return userHandler.getUserWithRecipients(userId)
    }

    override fun getAllWithRecipient(request: ServerRequest): ServerResponse {
        return userHandler.getAllWithRecipients()
    }

    override fun updateMyInformation(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID()
            ?: return ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
        val data = request.body<UserDTO.UpdateUserDTO>()
        return userHandler.updateUserInformation(userId, data)
    }
}