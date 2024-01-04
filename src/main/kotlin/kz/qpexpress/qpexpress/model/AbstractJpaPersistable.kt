package kz.qpexpress.qpexpress.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractJpaPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open var id: UUID? = null
    @CreatedDate
    open lateinit var createdAt: LocalDateTime
    @CreatedBy
    open lateinit var createdBy: UUID
    @LastModifiedDate
    open lateinit var updatedAt: LocalDateTime
    @LastModifiedBy
    open lateinit var updatedBy: UUID
}