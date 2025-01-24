package org.washcode.washpang.domain.handledItems.entity

import jakarta.persistence.*
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.global.comm.enums.LaundryCategory

@Entity
class HandledItems (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "laundryshop_id")
    val laundryshop: LaundryShop,

    val itemName: String,

    @Enumerated(EnumType.STRING)
    val category: LaundryCategory,
    val price: Int = 0
)