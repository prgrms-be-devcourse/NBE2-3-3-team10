package org.washcode.washpang.global.exception

import lombok.Getter

@Getter
open class BusinessException (private val errorCode: ErrorCode) : RuntimeException(errorCode.message) {
}