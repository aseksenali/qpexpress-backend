package kz.qpexpress.qpexpress.service

import jakarta.servlet.http.Part
import kz.qpexpress.qpexpress.dto.RecipientDTO
import kz.qpexpress.qpexpress.handler.IRecipientHandler
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.repository.FileDBRepository
import kz.qpexpress.qpexpress.util.toFileDB
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class RecipientService(
    private val recipientHandler: IRecipientHandler,
    private val fileDBRepository: FileDBRepository,
) : IRecipientService {
    private fun Part.decodeToString() = inputStream.bufferedReader().use { it.readText() }
    private fun MultiValueMap<String, Part>.getFileDBOrNull(
        fieldName: String,
        servletRequest: MultipartHttpServletRequest
    ): FileDB? {
        if (this["${fieldName}Id"] != null) {
            val id = this["${fieldName}Id"]?.firstOrNull()?.decodeToString()?.toUUID()
            if (id != null) {
                val fileDB = fileDBRepository.findByIdOrNull(id)
                return fileDB
            }
        } else {
            return servletRequest.getFile(fieldName)?.toFileDB()
        }
        return null
    }

    override fun getRecipients(request: ServerRequest): ServerResponse {
        return recipientHandler.getRecipients()
    }

    override fun createRecipient(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.FORBIDDEN
        ).build()
        val data = request.toRecipientDTO<RecipientDTO.CreateRecipientRequestDTO>()
        return recipientHandler.createRecipient(data, userId)
    }

    override fun getMyRecipients(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.FORBIDDEN
        ).build()
        return recipientHandler.getMyRecipients(userId)
    }

    override fun updateRecipient(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.FORBIDDEN
        ).build()
        val recipientId = request.pathVariable("id").toUUID() ?: return ServerResponse.notFound().build()
        val data = request.toRecipientDTO<RecipientDTO.UpdateRecipientRequestDTO>()
        return recipientHandler.updateRecipient(data, recipientId, userId)
    }

    override fun denyRecipient(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.FORBIDDEN
        ).build()
        val recipientId = request.pathVariable("id").toUUID() ?: return ServerResponse.notFound().build()
        val data = request.body<RecipientDTO.DenyRecipientRequestDTO>()
        return recipientHandler.denyRecipient(data, recipientId, userId)
    }

    override fun acceptRecipient(request: ServerRequest): ServerResponse {
        val userId = SecurityContextHolder.getContext().authentication.name.toUUID() ?: return ServerResponse.status(
            HttpStatus.FORBIDDEN
        ).build()
        val recipientId = request.pathVariable("id").toUUID() ?: return ServerResponse.notFound().build()
        return recipientHandler.acceptRecipient(recipientId, userId)
    }

    private inline fun <reified T> ServerRequest.toRecipientDTO(): T where T : RecipientDTO {
        val multipartData = this.multipartData()
        val singleData = multipartData.toSingleValueMap()
        val multipartRequest = this.servletRequest() as MultipartHttpServletRequest
        val documentSideA = multipartData.getFileDBOrNull("documentSideA", multipartRequest)
        val documentSideB = multipartData.getFileDBOrNull("documentSideB", multipartRequest)
        if (documentSideA == null) {
            throw Exception("Photo1 is null")
        }
        if (documentSideB == null) {
            throw Exception("Photo2 is null")
        }
        val firstName = singleData["firstName"]?.decodeToString() ?: throw Exception("firstName is null")
        val lastName = singleData["lastName"]?.decodeToString() ?: throw Exception("lastName is null")
        val patronymic = singleData["patronymic"]?.decodeToString() ?: throw Exception("patronymic is null")
        val iin = singleData["iin"]?.decodeToString() ?: throw Exception("iin is null")
        val district = singleData["district"]?.decodeToString() ?: throw Exception("district is null")
        val phoneNumber = singleData["phoneNumber"]?.decodeToString() ?: throw Exception("phoneNumber is null")
        val address = singleData["address"]?.decodeToString() ?: throw Exception("address is null")
        when (T::class.java) {
            RecipientDTO.CreateRecipientRequestDTO::class.java -> {
                return RecipientDTO.CreateRecipientRequestDTO(
                    firstName = firstName,
                    lastName = lastName,
                    patronymic = patronymic,
                    iin = iin,
                    documentSideA = documentSideA,
                    documentSideB = documentSideB,
                    district = district,
                    phoneNumber = phoneNumber,
                    address = address,
                ) as T
            }

            RecipientDTO.UpdateRecipientRequestDTO::class.java -> {
                val comment = singleData["comment"]?.decodeToString()
                return RecipientDTO.UpdateRecipientRequestDTO(
                    firstName = firstName,
                    lastName = lastName,
                    patronymic = patronymic,
                    iin = iin,
                    documentSideA = documentSideA,
                    documentSideB = documentSideB,
                    district = district,
                    phoneNumber = phoneNumber,
                    address = address,
                    comment = comment,
                ) as T
            }

            else -> throw Exception("Unknown class")
        }
    }
}