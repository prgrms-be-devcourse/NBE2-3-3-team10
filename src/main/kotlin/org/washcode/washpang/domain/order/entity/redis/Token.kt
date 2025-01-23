package org.washcode.washpang.domain.order.entity.redis

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(value = "token")
data class Token (
    @Id
    val id: Int,
    val refreshToken: String,
    @TimeToLive
    val expiration: Long
) {
    fun getToken(): String = refreshToken
}