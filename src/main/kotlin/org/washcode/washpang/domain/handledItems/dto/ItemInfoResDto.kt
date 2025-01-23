package org.washcode.washpang.domain.handledItems.dto

import org.washcode.washpang.global.comm.enums.LaundryCategory

data class ItemInfoResDto(
    val itemId: Int,
    val itemName: String,
    val category: LaundryCategory,
    val price: Int
)