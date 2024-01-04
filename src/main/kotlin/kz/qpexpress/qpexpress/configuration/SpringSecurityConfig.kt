package kz.qpexpress.qpexpress.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SpringSecurityConfig {
    @Bean
    fun securityWebFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            cors {
                configurationSource = UrlBasedCorsConfigurationSource().let {
                    val configuration = CorsConfiguration()
                    configuration.allowedOrigins = listOf("*")
                    configuration.allowedMethods = listOf("*")
                    configuration.allowedHeaders = listOf("*")
                    it.registerCorsConfiguration("/**", configuration)
                    it
                }
            }
            formLogin { disable() }
            httpBasic { disable() }
            oauth2ResourceServer { jwt {
                jwtAuthenticationConverter = jwtAuthenticationConverter()
            } }
            authorizeRequests {
                authorize("/actuator/**", permitAll)
                authorize(anyRequest, permitAll)
            }
        }
        return http.build()
    }

    @Bean
    fun springSecurityAuditorAware(): SpringSecurityAuditorAware {
        return SpringSecurityAuditorAware()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    private fun jwtAuthenticationConverter(): Converter<Jwt, out AbstractAuthenticationToken?> {
        val jwtConverter = JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter(KeycloakRealmRoleConverter())
        return jwtConverter
    }
}