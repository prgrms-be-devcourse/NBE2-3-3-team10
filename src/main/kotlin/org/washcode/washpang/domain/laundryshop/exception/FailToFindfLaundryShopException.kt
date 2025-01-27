package org.washcode.washpang.domain.laundryshop.exception

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class FailToFindfLaundryShopException: BusinessException(ErrorCode.FAIL_TO_FIND_LAUNDRYSHOP) {
}