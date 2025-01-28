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

    // DB
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 연결 에러"),

    // 클라이언트 오류 (Client Error, 400)
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Validation Error"),

    // User
    FAIL_TO_FIND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    NOT_EQUALS_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다"),
    NOT_DUPLICATE_EMAIL(HttpStatus.OK, "사용 가능한 이메일입니다"),

    // Auth
    TOKEN_NOT_EXPIRED(HttpStatus.OK, "accessToken 정상"),
    TOKEN_EXPIRED(HttpStatus.OK, "accessToken 만료됨"),

    // LaundryShop
    FAIL_TO_FIND_LAUNDRYSHOP(HttpStatus.NOT_FOUND, "존재하지 않는 세탁소입니다"),

    // OrderItems
    FAIL_TO_FIND_ORDERITEMS(HttpStatus.NOT_FOUND, "존재하지 않는 주문 아이템입니다"),

    // 카카오페이 결제 에러
    KAKAOPAY_READY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오페이 결제 준비 에러"),
    KAKAOPAY_APPROVE_REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "TID가 REDIS 안에 없습니다."),
    KAKAOPAY_APPROVE_FEIGN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오페이 승인 API 통신 실패"),
    KAKAOPAY_APPROVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오페이 결제 승인 에러");
}