package org.washcode.washpang.global.domain.kakaopay.redis.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("KakaoPaymentInfo")
data class KakaoPaymentInfo(
    @Id
    val key: String,  // Redis에서 사용할 키 (userId + ":" + cid)
    val partnerOrderId: Int,
    val tid: String
)