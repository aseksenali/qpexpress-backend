package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.FileDB
import org.springframework.http.MediaType
import java.util.UUID

sealed interface FileDTO {
    data class FileResponseDTO(
        val id: UUID,
        val name: String,
        val contentType: MediaType
    ) : FileDTO {
        constructor(file: FileDB): this(
            id = file.id!!,
            name = file.name,
            contentType = MediaType.parseMediaType(file.contentType))
    }
}