package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.FileDTO
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.repository.FileDBRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class FileHandler(
    private val fileDBRepository: FileDBRepository
): IFileHandler {
    override fun getFileById(fileId: UUID): Pair<ByteArray, Pair<MediaType, String>> {
        val file = fileDBRepository.findByIdOrNull(fileId) ?: throw Exception("File not found")
        val resource = file.data.getBytes(1, file.data.length().toInt())
        return Pair(resource, Pair(MediaType.parseMediaType(file.contentType), file.name))
    }

    override fun getFileMetaDataById(fileId: UUID): Pair<MediaType, String> {
        val file = fileDBRepository.findByIdOrNull(fileId) ?: throw Exception("File not found")
        return Pair(MediaType.parseMediaType(file.contentType), file.name)
    }

    override fun uploadFile(fileDB: FileDB): FileDTO.FileResponseDTO {
        val result = fileDBRepository.save(fileDB)
        return FileDTO.FileResponseDTO(result)
    }
}