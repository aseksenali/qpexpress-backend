package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.FileDTO
import kz.qpexpress.qpexpress.model.FileDB
import org.springframework.http.MediaType
import java.util.*

interface IFileHandler {
    fun getFileById(fileId: UUID): Pair<ByteArray, Pair<MediaType, String>>
    fun getFileMetaDataById(fileId: UUID): Pair<MediaType, String>
    fun uploadFile(fileDB: FileDB): FileDTO.FileResponseDTO
}