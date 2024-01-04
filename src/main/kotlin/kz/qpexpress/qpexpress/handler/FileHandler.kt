package kz.qpexpress.qpexpress.handler

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
}