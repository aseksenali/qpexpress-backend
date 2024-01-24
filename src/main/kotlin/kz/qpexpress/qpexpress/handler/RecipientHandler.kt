package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.RecipientDTO
import kz.qpexpress.qpexpress.model.Recipient
import kz.qpexpress.qpexpress.model.RecipientStatus
import kz.qpexpress.qpexpress.repository.FileDBRepository
import kz.qpexpress.qpexpress.repository.RecipientRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class RecipientHandler(
    private val recipientRepository: RecipientRepository,
    private val fileDBRepository: FileDBRepository,
) : IRecipientHandler {
    private final val logger = LoggerFactory.getLogger(RecipientHandler::class.java)
    override fun getRecipients(): ServerResponse {
        val recipients = recipientRepository.findAll().map { RecipientDTO.RecipientResponseDTO(it) }
        return ServerResponse.ok().body(recipients)
    }

    override fun createRecipient(data: RecipientDTO.CreateRecipientRequestDTO, userId: UUID): ServerResponse {
        logger.info("Create recipient request: $data")
        val photo1 = fileDBRepository.save(data.documentSideA)
        logger.info("Photo1 saved: ${photo1.id}")
        val photo2 = fileDBRepository.save(data.documentSideB)
        logger.info("Photo2 saved: ${photo2.id}")
        val savedEntity = recipientRepository.save(
            Recipient().also {
                it.userId = userId
                it.firstName = data.firstName
                it.lastName = data.lastName
                it.patronymic = data.patronymic
                it.iin = data.iin
                it.documentSideA = photo1
                it.documentSideB = photo2
                it.district = data.district
                it.phoneNumber = data.phoneNumber
                it.address = data.address
                it.status = RecipientStatus.PENDING
            }
        )
        logger.info("Recipient saved: ${savedEntity.id}")
        val result = RecipientDTO.RecipientResponseDTO(savedEntity)
        return ServerResponse.ok().body(result)
    }

    override fun getMyRecipients(userId: UUID): ServerResponse {
        val recipients = recipientRepository.findAllByUserId(userId).map { RecipientDTO.RecipientResponseDTO(it) }
        return ServerResponse.ok().body(recipients)
    }

    override fun updateRecipient(
        data: RecipientDTO.UpdateRecipientRequestDTO,
        recipientId: UUID,
        userId: UUID
    ): ServerResponse {
        val recipient = recipientRepository.findByIdOrNull(recipientId) ?: return ServerResponse.notFound().build()
        if (recipient.userId != userId) return ServerResponse.badRequest().build()
        val photo1 = fileDBRepository.save(data.documentSideA)
        val photo2 = fileDBRepository.save(data.documentSideB)
        recipient.apply {
            this.firstName = data.firstName
            this.lastName = data.lastName
            this.patronymic = data.patronymic
            this.iin = data.iin
            this.documentSideA = photo1
            this.documentSideB = photo2
            this.district = data.district
            this.phoneNumber = data.phoneNumber
            this.address = data.address
            this.status = RecipientStatus.PENDING
            this.comment = data.comment
        }
        val updatedEntity = recipientRepository.save(recipient)
        val result = RecipientDTO.RecipientResponseDTO(updatedEntity)
        return ServerResponse.ok().body(result)
    }

    override fun denyRecipient(
        data: RecipientDTO.DenyRecipientRequestDTO,
        recipientId: UUID,
        userId: UUID
    ): ServerResponse {
        val recipient = recipientRepository.findByIdOrNull(recipientId) ?: return ServerResponse.notFound().build()
        recipient.apply {
            this.status = RecipientStatus.INACTIVE
            this.comment = data.comment
        }
        val updatedEntity = recipientRepository.save(recipient)
        val result = RecipientDTO.RecipientResponseDTO(updatedEntity)
        return ServerResponse.ok().body(result)
    }

    override fun acceptRecipient(recipientId: UUID, userId: UUID): ServerResponse {
        val recipient = recipientRepository.findByIdOrNull(recipientId) ?: return ServerResponse.notFound().build()
        recipient.apply {
            this.status = RecipientStatus.ACTIVE
            this.comment = null
        }
        val updatedEntity = recipientRepository.save(recipient)
        val result = RecipientDTO.RecipientResponseDTO(updatedEntity)
        return ServerResponse.ok().body(result)
    }
}