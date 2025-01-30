package org.washcode.washpang.global.domain.kakaopay.redis.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.global.domain.kakaopay.redis.entity.KakaoPaymentInfo

@Repository
interface KakaoPaymentInfoRepository : CrudRepository<KakaoPaymentInfo, String>
