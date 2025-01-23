package org.washcode.washpang.domain.handledItems.dto

import org.washcode.washpang.global.comm.enums.LaundryCategory

data class HandledItemsResDTO(
    var itemName: String = "",
    var category: LaundryCategory,
    var price: Int = 0,
    var laundryId: Int = 0
) {
    // 세탁소 판매 항목 및 가격
}