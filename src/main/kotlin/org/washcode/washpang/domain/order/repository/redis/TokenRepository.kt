package org.washcode.washpang.domain.order.repository.redis

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.order.entity.redis.Token
import java.util.*

@Repository
interface TokenRepository : CrudRepository<Token, Long> {
    fun findById(id: Int): Token?
}

