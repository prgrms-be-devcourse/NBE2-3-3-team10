package org.washcode.washpang.global.domain.kakaopay.feign

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class KakaoPayApproveRedisErrorException: BusinessException(ErrorCode.KAKAOPAY_APPROVE_REDIS_ERROR)