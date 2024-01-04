package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.handler.IFileHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.http.ContentDisposition
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Service
class FileService(
    private val fileHandler: IFileHandler
): IFileService {
    override fun getFileById(request: ServerRequest): ServerResponse {
        val fileId = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        val (file, metaData) = fileHandler.getFileById(fileId)
        val (contentType, fileName) = metaData
        return ServerResponse.ok().contentType(contentType).headers {
            it.contentDisposition = ContentDisposition.inline().filename(fileName).build()
            it.contentLength = file.size.toLong()
        }.body(file)
    }
}