package org.washcode.washpang.domain.order.repository.db

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.order.entity.db.Payment
import org.washcode.washpang.domain.pickup.entity.Pickup

@Repository
interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByPickupId(pickupId: Long): Payment?

    fun findById(id: Int): Payment

    @Query("SELECT p.pickup FROM Payment p WHERE p.id = :id")
    fun findPickUpById(@Param("id") id: Int): Pickup
}