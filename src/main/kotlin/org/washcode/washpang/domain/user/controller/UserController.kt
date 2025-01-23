package org.washcode.washpang.domain.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.user.service.UserService
import org.washcode.washpang.global.exception.ResponseResult

@RestController
@Tag(name = "User API", description = "유저 인증 관련 API")
@RequestMapping("/api/user")
class UserController (private val userService: UserService) {

    @PostMapping("/check-login")
    @Operation(summary = "로그인 체크", description = "로그인 체크 API 입니다.")
    fun checkLogin(): ResponseResult { return userService.checkLogin() }
}