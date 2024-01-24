package kz.qpexpress.qpexpress.configuration

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import java.util.stream.Collectors


class KeycloakRealmRoleConverter : Converter<Jwt, Collection<GrantedAuthority>> {
    override fun convert(jwt: Jwt): Collection<GrantedAuthority>? {
        val realmAccess = jwt.getClaimAsMap("realm_access")
        if (realmAccess.isNullOrEmpty()) return emptyList()
        val rolesList = realmAccess["roles"] ?: return emptyList()
        if (rolesList !is List<*> || rolesList.isEmpty()) return emptyList()
        val roles = rolesList.filterIsInstance<String>()
        return roles.stream()
            .map { role ->
                SimpleGrantedAuthority(
                    role
                )
            }
            .collect(Collectors.toList())
    }
}