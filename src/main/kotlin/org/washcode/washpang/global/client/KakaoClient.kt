package org.washcode.washpang.global.client

import ch.qos.logback.core.model.Model
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import feign.FeignException
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*;
import org.springframework.http.ResponseCookie
import org.washcode.washpang.domain.user.entity.User
import org.washcode.washpang.domain.user.repository.UserRepository
import org.washcode.washpang.global.client.Dto.KakaoDto
import org.washcode.washpang.global.client.feign.client.KakaoApiServerClient
import org.washcode.washpang.global.client.feign.client.KakaoAuthServerClient

@Service
@Slf4j
class KakaoClient (
    private val kakaoAuthServerClient: KakaoAuthServerClient,
    private val kakaoApiServerClient: KakaoApiServerClient,
    private val userRepository: UserRepository
) {
    // Log 사용을 위한 선언
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Value("\${kakao.key.client-id}")
    lateinit var kakaoApiKey: String

    @Value("\${kakao.redirect-uri}")
    lateinit var redirectUri: String

    @Value("\${ACCESS_TOKEN_EXPIRATION_TIME}")
    lateinit var ACCESS_TOKEN_EXPIRATION_TIME: String

    @Value("\${REFRESH_TOKEN_EXPIRATION_TIME}")
    lateinit var REFRESH_TOKEN_EXPIRATION_TIME: String

    private fun RefreshTokenInHeader(refreshToken: String, response: HttpServletResponse): ResponseCookie {
        return ResponseCookie
            .from("REFRESHTOKEN", refreshToken)
            .domain("localhost")
            .path("/")
            .httpOnly(true)
            .maxAge(REFRESH_TOKEN_EXPIRATION_TIME.toLong())
            .build()
    }

    private fun getKakaoAccessToken(code: String): String {

        var res: KakaoDto.KakaoAccessToken? = null // 초기값 설정

        try {
            val dto = KakaoDto.AccessToken("authorization_code", kakaoApiKey, redirectUri, code)
            res = kakaoAuthServerClient.getAccessToken(dto.toString())

            return res.access_token
        } catch (e: FeignException) {
            log.error("=================================================================")
            log.error("카카오 Get AccessToken 실패")
            log.error(e.message)

            return ""
        }
    }

    private fun getKakaoUserData(accessToken: String): KakaoDto.Data? {
        val resBody: String
        val mapper = ObjectMapper()
        val res : KakaoDto.Data? = null
        log.info(accessToken)

        // 1. HTTP 요청 (https://kapi.kakao.com/v2/user/me 으로)
        try {
            resBody = kakaoApiServerClient.getKakaoUserInfo("Bearer $accessToken")
        } catch (e: FeignException) {
            log.error("=================================================================")
            log.error("카카오 Get UserData 실패")
            log.error(e.message)

            return null
        }

        // 2. 1에서 받아온 Body를 Json으로 파싱
        try {
            val j = mapper.readTree(resBody)
            val res = KakaoDto.Data(
                j.get("id").asLong(),
                j.get("kakao_account").get("email").asText(),
                j.get("properties").get("nickname").asText(),
                ""
            )
            return res
        } catch (e : JsonProcessingException) {
            log.error("=================================================================")
            log.error("카카오 Get UserData 실패")
            log.error(e.message)

            return null
        }
    }

    private fun createJwt(user: User, response: HttpServletResponse): String {
        try {
            /*
                1. AccessToken 발행 후, Body로 전송
                -> "1234" 대신에 accessToken를 넣어야 함 (JWT 구축 후 변경 필요)
             */
            // val accessToken = jwtProvider.generateAccessToken(user.getId(),user.getRole())
            val resAccessToken = Pair("AccessToken", "1234")

            /*
                2. RefreshToken 발행 후, Header(쿠키)로 전송
                -> "R1234" 대신에 refreshToken를 넣어야 함 (JWT 구축 후 변경 필요)
             */
            // val refreshToken = jwtProvider.generateRefreshToken(user.getId(),user.getRole())
            val resRefreshToken = RefreshTokenInHeader("R1234", response).toString()

            // 3. RT를 헤더에 쿠키로 추가
            response.addHeader(HttpHeaders.SET_COOKIE, resRefreshToken)

            // 4. Body에 AT를 추가
            return "1234"
        } catch (e: Exception) {
            log.error("=================================================================")
            log.error(e.message)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
            response.setHeader("error", e.message)

            return e.message.toString()
        }
    }

//    fun login(code: String, model: Model ,response: HttpServletResponse): String {
//
//        // 1. "인가 코드"로 "액세스 토큰" 요청
//        val accessToken = getKakaoAccessToken(code)
//
//        if (accessToken == "") {
//            return "glober/Login"
//        }
//
//        // 2. "액세스 토큰"으로 "사용자 정보" 요청
//        val result = getKakaoUserData(accessToken)
//
//        // 3. 카카오로 가입되어 있지 확인
//        // 3-1 가입되어 있지 않다면 회원가입 페이지로 이동
//        val user = userRepository.findIdByKakaoId(result.id);
//        if (user.isEmpty()) {
//            model.addAttribute("kakaoUserData", result);
//            return "glober/register";
//        }
//
//        // 3-2 가입이 되어 있으면 쿠키를 통해 토큰 발행 후, 로그인 진행
//        model.addAttribute("AccessToken", createJwt(user.get(), response))
//        return "glober/kakaoLoginWait";
//
//    }
}