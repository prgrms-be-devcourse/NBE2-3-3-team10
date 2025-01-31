package org.washcode.washpang.domain.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.user.dto.UserDto
import org.washcode.washpang.domain.user.service.UserService
import org.washcode.washpang.global.domain.kakao.client.KakaoClient
import org.washcode.washpang.global.exception.ResponseResult

@RestController
@Tag(name = "User API", description = "유저 인증 관련 API")
@RequestMapping("/api/user")
class UserController (
    private val userService: UserService,
    private val kakaoClient: KakaoClient) {

    @PostMapping("/check-login")
    @Operation(summary = "로그인 체크", description = "로그인 체크 API 입니다.")
    fun checkLogin(request: HttpServletRequest,response: HttpServletResponse): ResponseResult { return userService.checkLogin(request,response) }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입 API 입니다.")
    fun signup(@RequestBody @Valid registerReqDTO: UserDto.RegisterReq): ResponseResult {
        return userService.signup(registerReqDTO)
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API 입니다.")
    fun login(@RequestBody @Valid loginReqDTO: UserDto.LoginReq, response: HttpServletResponse): ResponseResult {
        return userService.login(loginReqDTO, response)
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 API 입니다.")
    fun logout(response: HttpServletResponse): ResponseResult {
        return userService.logout(response)
    }

    @GetMapping
    @Operation(summary = "회원정보 조회", description = "회원정보를 조회하는 API 입니다.")
    fun getUser(@AuthenticationPrincipal id: Int): ResponseResult {
        return userService.getUser(id)
    }

    @PutMapping
    @Operation(summary = "회원정보 수정", description = "회원정보를 수정하는 API 입니다.")
    fun updateUser(
        @AuthenticationPrincipal id: Int,
        @RequestBody userUpdateReqDTO: UserDto.UpdateReq
    ): ResponseResult {
        return userService.updateUser(id, userUpdateReqDTO)
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "회원정보를 삭제하는 API 입니다.")
    fun deleteUser(@AuthenticationPrincipal id: Int): ResponseResult {
        return userService.deleteUser(id)
    }

    @GetMapping("/role")
    @Operation(summary = "회원등급 조회", description = "회원의 등급만 조회하는 API 입니다")
    fun getUserRole(@AuthenticationPrincipal id: Int): ResponseResult {
        return userService.getUserRole(id)
    }

    @GetMapping("/address")
    @Operation(summary = "회원주소 조회", description = "회원의 주소만 조회하는 API 입니다")
    fun getUserAddress(@AuthenticationPrincipal id: Int): ResponseResult {
        return userService.getUserAddress(id)
    }

    @GetMapping("/check-email")
    @Operation(summary = "이메일 중복 체크", description = "이메일 중복 체크 API 입니다.")
    fun checkEmail(@RequestParam email: String): ResponseResult {
        return userService.checkEmailDuplication(email)
    }

}