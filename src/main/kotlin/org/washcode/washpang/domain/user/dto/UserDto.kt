package org.washcode.washpang.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.washcode.washpang.global.comm.enums.UserRole

class UserDto private constructor(){
    // 사용자 로그인 (공통)
    data class LoginReq (
        @Schema(description = "사용자 ID", example = "customer@test.com")
        val email: String,

        @Schema(description = "비밀번호", example = "123")
        val password: String
    )

    data class RegisterReq (
        @Schema(description = "사용자 ID", example = "customer@test.com")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        val email: String,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Schema(description = "비밀번호", example = "123")
        val password: String,

        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        @Schema(description = "사용자 이름", example = "홍길동")
        val name: String,

        @NotBlank(message = "주소는 필수 입력 항목입니다.")
        @Schema(description = "주소", example = "서울시 성북구")
        val baseAddress: String,

        @NotBlank(message = "상세 주소는 필수 입력 항목입니다.")
        @Schema(description = "상세 주소", example = "빌라 1층 103호")
        val detailedAddress: String,

        @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
        @Schema(description = "사용자 이름", example = "010-1234-5678")
        val phone: String,

        @Schema(description = "사용자 권한", example = "USER")
        val role: UserRole,

        @Schema(description = "카카오 ID")
        val kakaoId: Long
    )

    data class MyPageRes (
        val role: UserRole,
        val name: String
    )

    data class AddressRes (
        val baseAddress: String,
        val detailedAddress: String,
    )

    data class ProfileRes (
        val name: String,
        val email: String,
        val baseAddress: String,
        val detailedAddress: String,
        val phone: String
    )

    // 회원 정보 요청시 ->상세보기 페이지
    data class UserRes (
        val name: String,
        val baseAddress: String,
        val detailedAddress: String,
        val phone: String
    )

    // 사용자 정보 수정 (공통)
    data class UpdateReq (
        val baseAddress: String,
        val detailedAddress: String,
        val phone: String,
        val password: String?
    )
}