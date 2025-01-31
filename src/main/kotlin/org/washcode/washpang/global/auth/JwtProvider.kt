package org.washcode.washpang.global.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.washcode.washpang.domain.order.entity.redis.Token
import org.washcode.washpang.domain.order.repository.redis.TokenRepository
import org.washcode.washpang.global.comm.enums.UserRole
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider @Autowired constructor(private val tokenRepository: TokenRepository) {
    // 토큰(Access,Refresh) 만료시간(ms)
    @Value("\${ACCESS_TOKEN_EXPIRATION_TIME}")
    private val ACCESS_EXPIRATION_TIME = 0 // 10분

    @Value("\${REFRESH_TOKEN_EXPIRATION_TIME}")
    private val REFRESH_EXPIRATION_TIME = 0 // 1일

    private val SECRETKEY: SecretKey =
        Keys.secretKeyFor(SignatureAlgorithm.HS256)

    // Access 토큰 생성
    fun generateAccessToken(id: Int, role: UserRole): String {
        val claims = Jwts.claims().setSubject(id.toString())
        claims["role"] = role
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + ACCESS_EXPIRATION_TIME))
            .signWith(SECRETKEY, SignatureAlgorithm.HS256)
            .compact()
    }

    // Refresh 토큰 생성
    fun generateRefreshToken(id: Int, role: UserRole): String {
        val claims = Jwts.claims().setSubject(id.toString())
        claims["role"] = role
        val now = Date()

        // RefreshToken(RT) 생성
        val refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + REFRESH_EXPIRATION_TIME))
            .signWith(SECRETKEY, SignatureAlgorithm.HS256)
            .compact()

        try {
            // RT 캡슐화
            val token = Token(id, refreshToken, REFRESH_EXPIRATION_TIME.toLong())

            // RT를 Redis에 저장
            tokenRepository.save(token)

            return refreshToken
        } catch (e: Exception) {
            println("[Redis] RefreshToken save failed: " + e.message)
            return "null"
        }
    }

    // JWT 검증(변조 및 만료여부 확인)
    fun validateToken(token: String): Boolean {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(SECRETKEY)
                .build()
                .parseClaimsJws(token)
            return !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            println("Token validation failed: " + e.message)
            return false
        }
    }

    // 헤더에서 Access 토큰 가져오기
    fun resolveAccessToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    // 헤더에서 Refresh 토큰 가져오기
    fun resolveRefreshToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Refresh")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    // 토큰에서 id 반환
    fun getId(token: String): Int {
        return Jwts.parserBuilder()
            .setSigningKey(SECRETKEY)
            .build()
            .parseClaimsJws(token)
            .body
            .subject.toInt()
    }

    // 토큰에서 role 반환
    fun getRole(token: String): UserRole {
        return UserRole.valueOf(
            Jwts.parserBuilder()
                .setSigningKey(SECRETKEY)
                .build()
                .parseClaimsJws(token)
                .body
                .get("role", String::class.java)
        )
    }
}
