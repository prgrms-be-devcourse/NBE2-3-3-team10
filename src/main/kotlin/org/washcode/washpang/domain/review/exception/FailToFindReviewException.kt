package org.washcode.washpang.domain.review.exception

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class FailToFindReviewException: BusinessException(ErrorCode.FAIL_TO_FIND_REVIEW) {
}