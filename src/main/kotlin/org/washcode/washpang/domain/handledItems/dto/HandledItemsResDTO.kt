package org.washcode.washpang.domain.handledItems.dto

import lombok.Getter
import lombok.Setter
import lombok.ToString
import org.washcode.washpang.global.comm.enums.LaundryCategory


data class HandledItemsResDTO(
    var itemName: String? = null,
    var category: LaundryCategory? = null,
    var price: Int = 0,

    var laundryId: Int = 0
) {
    // 세탁소 판매 항목 및 가격
}
