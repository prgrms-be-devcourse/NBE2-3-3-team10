package org.washcode.washpang.domain.handledItems.entity

import jakarta.persistence.*
import lombok.Data
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.global.comm.enums.LaundryCategory

@Entity
@Data
class HandledItems {
    // 가격표
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @ManyToOne
    @JoinColumn(name = "laundryshop_id")
    var laundryshop: LaundryShop? = null

    var itemName: String? = null

    @Enumerated(EnumType.STRING)
    var category: LaundryCategory? = null
    var price: Int = 0
}
