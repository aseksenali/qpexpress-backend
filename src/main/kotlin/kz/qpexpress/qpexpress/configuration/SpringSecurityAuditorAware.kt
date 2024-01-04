package kz.qpexpress.qpexpress.configuration

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class SpringSecurityAuditorAware: AuditorAware<UUID> {
    override fun getCurrentAuditor(): Optional<UUID> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            return Optional.empty()
        }
        return Optional.of(UUID.fromString(authentication.name))
    }
}