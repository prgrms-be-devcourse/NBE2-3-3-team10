package org.washcode.washpang.domain.handledItems.entity

import jakarta.persistence.*
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.global.comm.enums.LaundryCategory

@Entity
class HandledItems (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @ManyToOne
    @JoinColumn(name = "laundryshopId")
    var laundryshop: LaundryShop,

    var itemName: String,

    @Enumerated(EnumType.STRING)
    var category: LaundryCategory,
    var price: Int
)