package org.washcode.washpang.domain.review.repository

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.review.entity.Review

@Repository
interface ReviewRepository: JpaRepository<Review, Long> {
    
}