package org.washcode.washpang.domain.user.service

import jakarta.servlet.http.HttpServletRequest
import lombok.extern.java.Log
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.washcode.washpang.global.exception.ErrorCode
import org.washcode.washpang.global.exception.ResponseResult

@Service
class UserService {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    fun checkLogin(): ResponseResult{
        val developToken = mapOf("accessToken" to "123456")
        return ResponseResult(developToken)
    }
}