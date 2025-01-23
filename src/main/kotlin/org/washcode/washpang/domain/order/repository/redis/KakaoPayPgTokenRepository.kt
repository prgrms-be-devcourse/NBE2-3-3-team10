package org.washcode.washpang.domain.order.repository.redis

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.order.entity.redis.KakaoPayPgToken

@Repository
interface KakaoPayPgTokenRepository : CrudRepository<KakaoPayPgToken, String>

