package org.washcode.washpang.global.config

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.washcode.washpang.global.auth.JwtProvider
import org.washcode.washpang.global.auth.JwtAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtProvider: JwtProvider) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .cors().and() // CORS 설정 추가
            .authorizeHttpRequests { auth -> auth
                // .requestMatchers("/api/**").permitAll()
                // .requestMatchers("/swagger-ui/**").permitAll()
                // .requestMatchers("/v3/api-docs/**").permitAll()
                // .requestMatchers("/").permitAll()
                // .anyRequest().authenticated()
                .anyRequest().permitAll()
            }
            .addFilterBefore(JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
