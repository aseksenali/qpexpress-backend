package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.FileDTO
import kz.qpexpress.qpexpress.handler.IFileHandler
import kz.qpexpress.qpexpress.model.FileDB
import kz.qpexpress.qpexpress.util.toUUID
import org.hibernate.engine.jdbc.BlobProxy
import org.springframework.http.ContentDisposition
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class FileService(
    private val fileHandler: IFileHandler
) : IFileService {
    override fun getFileById(request: ServerRequest): ServerResponse {
        val fileId = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        val (contentType, name) = fileHandler.getFileMetaDataById(fileId)
        return ServerResponse.ok().body(FileDTO.FileResponseDTO(fileId, name, contentType))
    }

    override fun downloadFileById(request: ServerRequest): ServerResponse {
        val fileId = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        val (file, metaData) = fileHandler.getFileById(fileId)
        val (contentType, fileName) = metaData
        return ServerResponse.ok().contentType(contentType).headers {
            it.contentDisposition = ContentDisposition.inline().filename(fileName).build()
            it.contentLength = file.size.toLong()
        }.body(file)
    }

    override fun uploadFile(request: ServerRequest): ServerResponse {
        val multipartData = request.multipartData()
        val fileField = multipartData["file"]
        val response = if (fileField != null) {
            val multipartRequest = request.servletRequest() as MultipartHttpServletRequest
            val file = multipartRequest.getFile("file") ?: return ServerResponse.badRequest().build()
            val fileName = file.originalFilename ?: UUID.randomUUID().toString()
            val contentType = file.contentType
            val invoiceData = BlobProxy.generateProxy(file.inputStream, file.size)
            val fileEntity = FileDB().also {
                it.name = fileName
                it.contentType = contentType.toString()
                it.data = invoiceData
            }
            fileHandler.uploadFile(fileEntity)
        } else return ServerResponse.badRequest().build()
        return ServerResponse.ok().body(response)
    }
}