package org.washcode.washpang.domain.review.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.domain.user.entity.User
import java.sql.Timestamp

@Entity
class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    @ManyToOne
    @JoinColumn(name = "pickupId")
    lateinit var pickup: Pickup

    @ManyToOne
    @JoinColumn(name = "laundryShopId")
    lateinit var laundryShop: LaundryShop

    lateinit var content: String

    @CreationTimestamp
    lateinit var createAt: Timestamp
}
