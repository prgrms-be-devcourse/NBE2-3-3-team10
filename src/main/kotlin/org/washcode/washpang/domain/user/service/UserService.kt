package org.washcode.washpang.domain.user.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService {

    public fun checkLogin(request: HttpServletRequest): ResponseEntity<*> {
        return ResponseEntity
            .ok()
            .body("Develop AccessToken")
    }

}