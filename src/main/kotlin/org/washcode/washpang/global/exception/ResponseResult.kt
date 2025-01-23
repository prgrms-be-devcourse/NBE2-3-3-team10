package org.washcode.washpang.global.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.RequiredArgsConstructor
import java.time.Instant

@Getter
data class ResponseResult(
    val code: Int? = null,
    val message: String? = null,
    val timestamp: String = Instant.now().toString(),
    val data: Any? = null
) {
    constructor(codes: ErrorCode) : this(
        codes.status.value(), codes.message
    )

    constructor(code: ErrorCode, data: Any) : this(
        code.status.value(), code.message,
        data = data
    )

    constructor(data: Any) : this(
        code = ErrorCode.SUCCESS.status.value(),
        message = ErrorCode.SUCCESS.message,
        data = data
    )

    override fun toString() =
        "ResponseResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", data=" + data +
                '}'
}