package org.washcode.washpang.domain.order.exception

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class FailToFindOrderItemsException: BusinessException(ErrorCode.FAIL_TO_FIND_ORDERITEMS)
