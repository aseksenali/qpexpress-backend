package kz.qpexpress.qpexpress.repository;

import kz.qpexpress.qpexpress.model.FileDB
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FileDBRepository : JpaRepository<FileDB, UUID> {
}