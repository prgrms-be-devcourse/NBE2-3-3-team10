package org.washcode.washpang.global.domain.kakaopay.exception

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class KakaoPayApproveFeignException() : BusinessException(ErrorCode.KAKAOPAY_APPROVE_FEIGN_ERROR)