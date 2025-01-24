package org.washcode.washpang.domain.inquiry.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop

@Repository
interface InquiryRepository : JpaRepository<LaundryShop, Long>
