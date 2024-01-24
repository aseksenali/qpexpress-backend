package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.UserDTO
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IUserHandler {
    fun getUserInformation(id: UUID): ServerResponse
    fun getAll(): ServerResponse
    fun getUserWithRecipients(id: UUID): ServerResponse
    fun getAllWithRecipients(): ServerResponse
    fun updateUserInformation(id: UUID, data: UserDTO.UpdateUserDTO): ServerResponse
}