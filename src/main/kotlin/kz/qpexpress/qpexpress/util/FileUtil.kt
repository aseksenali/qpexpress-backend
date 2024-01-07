package kz.qpexpress.qpexpress.util

import kz.qpexpress.qpexpress.model.FileDB
import org.hibernate.engine.jdbc.BlobProxy
import org.springframework.web.multipart.MultipartFile
import java.util.*

fun MultipartFile.toFileDB(): FileDB {
    val fileName = this.originalFilename ?: UUID.randomUUID().toString()
    val contentType = this.contentType
    val invoiceData = BlobProxy.generateProxy(this.inputStream, this.size)
    return FileDB().also {
        it.name = fileName
        it.contentType = contentType.toString()
        it.data = invoiceData
    }
}