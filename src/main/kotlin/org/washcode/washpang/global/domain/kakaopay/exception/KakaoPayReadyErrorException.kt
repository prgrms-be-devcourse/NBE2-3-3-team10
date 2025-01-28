package org.washcode.washpang.global.domain.kakaopay.exception

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class KakaoPayReadyErrorException: BusinessException(ErrorCode.KAKAOPAY_READY_ERROR){
}