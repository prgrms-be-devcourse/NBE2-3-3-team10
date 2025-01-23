package org.washcode.washpang.domain.handledItems.dto

import lombok.Getter
import lombok.Setter
import lombok.ToString
import org.washcode.washpang.global.comm.enums.LaundryCategory

data class HandledItemsResDTO(
    val itemName: String? = "",
    val category: LaundryCategory? = null,
    val price: Int = 0,
    val laundryId: Int = 0
)
