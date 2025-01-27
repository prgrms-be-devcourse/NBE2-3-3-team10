package org.washcode.washpang.domain.user.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.washcode.washpang.domain.order.entity.redis.Token
import org.washcode.washpang.domain.order.repository.redis.TokenRepository
import org.washcode.washpang.domain.user.dto.UserDto
import org.washcode.washpang.domain.user.entity.User
import org.washcode.washpang.domain.user.exception.NoUserDataException
import org.washcode.washpang.domain.user.repository.UserRepository
import org.washcode.washpang.global.auth.JwtProvider
import org.washcode.washpang.global.comm.enums.UserRole
import org.washcode.washpang.global.exception.ErrorCode
import org.washcode.washpang.global.exception.ResponseResult

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
    private val tokenRepository: TokenRepository
) {
    @Value("\${REFRESH_TOKEN_EXPIRATION_TIME}")
    private val REFRESH_TOKEN_EXPIRATION_TIME = 0
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    // 토큰 유효성 검사
    fun checkLogin(request: HttpServletRequest, response: HttpServletResponse): ResponseResult {
        // 토큰 가져오기
        val accessToken = jwtProvider.resolveAccessToken(request)
        val cookies = request.cookies
        var refreshToken: String? = null

        for (cookie in cookies) {
            if (cookie.name == "REFRESHTOKEN") {
                refreshToken = cookie.value
            }
        }
        // AccessToken 유효성 확인
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            return ResponseResult(ErrorCode.TOKEN_NOT_EXPIRED)
            // RefreshToken 유효성 확인 후 AccessToken 재발급
        } else if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
            // RefreshToken 유효성 확인 후, UserId 가져오기
            val userId = jwtProvider.getId(refreshToken)

            // Redis에서 RefreshToken 가져오기
            // 없으면 람다로 에러 발생
            val token: Token = tokenRepository.findById(userId) ?: throw NoUserDataException()

            if (token.getToken().equals(refreshToken)) {
                val role: UserRole = jwtProvider.getRole(refreshToken)

                // RefreshToken 재발급
                // RTR 방식 (Refresh Token Rotation) -> AccessToken 발급 시, 새로운 RefreshToken도 같이 발급
                refreshToken = jwtProvider.generateRefreshToken(userId, role)

                val cookie: Cookie = Cookie("REFRESHTOKEN", refreshToken)
                cookie.domain = "localhost"
                cookie.path = "/"
                cookie.maxAge = REFRESH_TOKEN_EXPIRATION_TIME
                response.addCookie(cookie)

                // AccessToken 반환
                val newAccessToken = jwtProvider.generateAccessToken(userId, role)
                val responseAccessToken: MutableMap<String, String> = HashMap()
                responseAccessToken["accessToken"] = newAccessToken

                return ResponseResult(responseAccessToken)
            } else {
                throw IllegalArgumentException(userId.toString() + "번 유저 Redis에 있는 RefreshToken과 다름, 탈취 위험있음")
            }
            // 모든 토큰 유효성 검사 실패
        } else {
            return ResponseResult(ErrorCode.TOKEN_EXPIRED)
        }
    }

    fun signup(registerReqDTO: UserDto.RegisterReq): ResponseResult {
        try {
            // 이메일 검증
            if (userRepository.findByEmailExists(registerReqDTO.email)) {
                return ResponseResult(ErrorCode.DUPLICATE_EMAIL)
            }

            val encodePassword: String = passwordEncoder.encode(registerReqDTO.password)

            val user: User = User(
                id = 0,
                kakaoId = null,
                name = registerReqDTO.name,
                password = encodePassword,
                baseAddress = registerReqDTO.baseAddress,
                detailedAddress = registerReqDTO.detailedAddress,
                phone = registerReqDTO.phone,
                email = registerReqDTO.email,
                role = registerReqDTO.role)

            userRepository.save(user)

            return ResponseResult(ErrorCode.SUCCESS)
        } catch (e: Exception) {
            println("[Error] " + e.message)
            return ResponseResult(ErrorCode.DB_ERROR)
        }
    }

    fun login(loginReqDTO: UserDto.LoginReq, response: HttpServletResponse): ResponseResult {
        try {
            // 이메일 검증
            if (!userRepository.findByEmailExists(loginReqDTO.email)) {
                return ResponseResult(ErrorCode.FAIL_TO_FIND_USER)
            }
            // 비밀번호 검증
            if (!passwordEncoder.matches(
                    loginReqDTO.password,
                    userRepository.findByPassword(loginReqDTO.email)
                )
            ) {
                return ResponseResult(ErrorCode.NOT_EQUALS_PASSWORD)
            }

            val user: User =
                userRepository.findByEmail(loginReqDTO.email) ?: throw NoUserDataException()

            val accessToken: String = jwtProvider.generateAccessToken(user.id, user.role)
            val refreshToken: String = jwtProvider.generateRefreshToken(user.id, user.role)

            val responseAccessToken: MutableMap<String, String> = HashMap()
            responseAccessToken["accessToken"] = accessToken

            val cookie: Cookie = Cookie("REFRESHTOKEN", refreshToken)
            cookie.domain = "localhost"
            cookie.path = "/"
            cookie.maxAge = REFRESH_TOKEN_EXPIRATION_TIME
            response.addCookie(cookie)

            return ResponseResult(responseAccessToken)
        } catch (e: Exception) {
            println("[Error] " + e.message)
            return ResponseResult(ErrorCode.DB_ERROR)
        }
    }

    fun logout(response: HttpServletResponse): ResponseResult {
        // Access Token 은 Front 에서 삭제 필요
        // Refresh Token 유효시간을 0으로 설정
        val cookie: Cookie = Cookie("REFRESHTOKEN", "")
        cookie.domain = "localhost"
        cookie.path = "/"
        cookie.maxAge = 0
        response.addCookie(cookie)

        return ResponseResult(ErrorCode.SUCCESS)
    }

    // 유저 정보 가져오기
    fun getUser(id: Int): ResponseResult {
        val user: User = userRepository.findById(id) ?: throw NoUserDataException()

        return ResponseResult(
            UserDto.ProfileRes(
                user.name,
                user.email,
                user.baseAddress,
                user.detailedAddress,
                user.phone
                )
        )
    }

    @Transactional
    fun updateUser(id: Int, userUpdateReqDTO: UserDto.UpdateReq): ResponseResult {
        try {
            val user: User = userRepository.findById(id) ?: throw NoUserDataException()
            user.baseAddress = userUpdateReqDTO.baseAddress
            user.detailedAddress = userUpdateReqDTO.detailedAddress
            user.phone = userUpdateReqDTO.phone

            if (userUpdateReqDTO.password != null) {
                user.password = userUpdateReqDTO.password
            }

            return ResponseResult(ErrorCode.SUCCESS)
        } catch (e: NoUserDataException) {
            println("[Error] " + e.message)
            return ResponseResult(ErrorCode.FAIL_TO_FIND_USER)
        }
    }

    fun deleteUser(id: Int): ResponseResult {
        try {
            userRepository.deleteById(id.toLong())
            return ResponseResult(ErrorCode.SUCCESS)
        } catch (e: Exception) {
            println("[Error] " + e.message)
            return ResponseResult(ErrorCode.DB_ERROR)
        }
    }

    fun getRefreshToken(refreshToken: String): ResponseCookie {
        return ResponseCookie
            .from("REFRESHTOKEN", refreshToken) // 추후 토큰값 추가
            .domain("localhost")
            .path("/")
            .httpOnly(true)
            .maxAge(REFRESH_TOKEN_EXPIRATION_TIME.toLong())
            .build()
    }

    fun getUserRole(id: Int): ResponseResult {
        return try {
            val user = userRepository.findById(id) ?: throw NoUserDataException()
            val userRole = UserDto.MyPageRes(user.role,user.name)
            ResponseResult(userRole)
        } catch (e: NoUserDataException) {
            ResponseResult(ErrorCode.FAIL_TO_FIND_USER)
        }
    }

    fun getUserAddress(id: Int): ResponseResult {
        return try {
            val userAddress = userRepository.findAddressById(id)
            ResponseResult(userAddress)
        } catch (e: Exception) {
            ResponseResult(ErrorCode.DB_ERROR)
        }
    }

    fun getUserById(id: Int): User {
        return userRepository.findById(id) ?: throw NoUserDataException()
    }

    fun checkEmailDuplication(email: String): ResponseResult {
        try {
            return if (userRepository.findByEmailExists(email)) {
                ResponseResult(ErrorCode.DUPLICATE_EMAIL)
            } else {
                ResponseResult(ErrorCode.NOT_DUPLICATE_EMAIL)
            }
        } catch (e: Exception) {
            println("[Error] " + e.message)
            return ResponseResult(ErrorCode.DB_ERROR)
        }
    }
}