package org.washcode.washpang.global.domain.kakaopay.exception

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class KakaoPayApproveErrorException: BusinessException(ErrorCode.KAKAOPAY_APPROVE_ERROR)