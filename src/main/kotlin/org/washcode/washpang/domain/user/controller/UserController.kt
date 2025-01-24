package org.washcode.washpang.domain.user.controller

import ch.qos.logback.core.model.Model
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.user.service.UserService
import org.washcode.washpang.global.client.KakaoClient
import org.washcode.washpang.global.exception.ResponseResult

@RestController
@Tag(name = "User API", description = "유저 인증 관련 API")
@RequestMapping("")
class UserController (
    private val userService: UserService,
    private val kakaoClient: KakaoClient) {

    @PostMapping("/check-login")
    @Operation(summary = "로그인 체크", description = "로그인 체크 API 입니다.")
    fun checkLogin(): ResponseResult { return userService.checkLogin() }

    @GetMapping("/kakaoLogin")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API 입니다.")
    fun kakaoLogin(@RequestParam code: String, model: Model, response: HttpServletResponse): Any { return kakaoClient.login(code, model, response) }
}