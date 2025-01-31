package org.washcode.washpang.domain.order.dto

import org.washcode.washpang.global.comm.enums.PickupStatus
import java.sql.Timestamp

data class OrderListResDto (
    val shopName: String,
    val pickupId: Int,
    val status: PickupStatus,
    val createdAt: Timestamp
)