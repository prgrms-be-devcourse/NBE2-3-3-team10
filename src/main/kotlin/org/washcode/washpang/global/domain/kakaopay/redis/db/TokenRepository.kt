package org.washcode.washpang.global.domain.kakaopay.redis.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.global.domain.kakaopay.redis.entity.Token
import java.util.*

@Repository
interface TokenRepository : CrudRepository<Token, Long> {
    fun findById(id: Int): Token?
}

