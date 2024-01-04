package kz.qpexpress.qpexpress.model

import jakarta.persistence.Entity
import jakarta.persistence.Lob
import jakarta.persistence.Table
import java.sql.Blob

@Entity
@Table(name = "files")
class FileDB: AbstractJpaPersistable() {
    lateinit var name: String
    lateinit var contentType: String
    @Lob
    lateinit var data: Blob
}