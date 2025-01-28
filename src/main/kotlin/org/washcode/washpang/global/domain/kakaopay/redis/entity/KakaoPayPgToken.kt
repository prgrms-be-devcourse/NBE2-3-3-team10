package org.washcode.washpang.global.domain.kakaopay.redis.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("KakaoPayPgToken")
data class KakaoPayPgToken(
    @Id
    val key: String,           // Redis Key
    val partnerOrderId: Int    // Redis Value
)