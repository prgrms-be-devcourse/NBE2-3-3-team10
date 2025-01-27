package org.washcode.washpang.domain.user.exception

import org.washcode.washpang.global.exception.BusinessException
import org.washcode.washpang.global.exception.ErrorCode

class NoUserDataException: BusinessException(ErrorCode.FAIL_TO_FIND_USER)