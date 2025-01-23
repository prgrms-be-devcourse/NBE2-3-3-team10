package org.washcode.washpang.domain.pickup.entity

import jakarta.persistence.*
import org.washcode.washpang.domain.handledItems.entity.HandledItems

@Entity
class PickupItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickupId")
    val pickup: Pickup,

    @ManyToOne
    @JoinColumn(name = "handledItemsId")
    val handledItems: HandledItems,

    val quantity: Int,

    @JoinColumn(name = "totalPrice")
    val totalPrice: Int,
)