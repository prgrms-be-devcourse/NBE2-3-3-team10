package org.washcode.washpang.domain.pickup.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.pickup.entity.PickupItem

@Repository
interface PickupItemRepository: JpaRepository<PickupItem, Long> {
    fun findByPickupId(pickupId: Long): List<PickupItem>
}