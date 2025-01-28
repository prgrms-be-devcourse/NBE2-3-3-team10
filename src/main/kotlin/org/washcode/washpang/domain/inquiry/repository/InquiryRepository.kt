package org.washcode.washpang.domain.inquiry.repository

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.inquiry.dto.InquiryDto
import org.washcode.washpang.domain.inquiry.entity.Inquiry

@Repository
interface InquiryRepository: JpaRepository<Inquiry, Long> {
    fun findByUserId(userId: Int): Inquiry

    fun findListByUserId(userId: Int): List<Inquiry>

    @Query("select i from Inquiry i where i.laundryShop.id = :laundryId")
    fun findListByLaundryId(laundryId: Int): List<Inquiry>
}