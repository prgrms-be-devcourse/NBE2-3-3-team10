package org.washcode.washpang.domain.order.repository.redis

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.order.entity.redis.KakaoPaymentInfo

@Repository
interface KakaoPaymentInfoRepository : CrudRepository<KakaoPaymentInfo, String>
