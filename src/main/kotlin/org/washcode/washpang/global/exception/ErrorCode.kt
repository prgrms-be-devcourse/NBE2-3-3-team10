package org.washcode.washpang.global.exception

import lombok.Getter
import org.springframework.http.HttpStatus

@Getter
enum class ErrorCode (
    val status: HttpStatus,
    val message: String
) {
    // 에러 코드 예시
    TEST_STATUS(HttpStatus.OK, "health"),

    // 성공 (Success, 200)
    SUCCESS(HttpStatus.OK, "성공"),

    // 클라이언트 오류 (Client Error, 400)
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Validation Error"),

    // User
    FAIL_TO_FIND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다"),

    // DB
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 연결 에러");
}