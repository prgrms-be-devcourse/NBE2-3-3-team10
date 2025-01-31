package org.washcode.washpang.domain.pickup.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.order.entity.Payment
import org.washcode.washpang.domain.user.entity.User
import org.washcode.washpang.global.comm.enums.PickupStatus
import java.sql.Timestamp

@Entity
class Pickup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "laundryshopId")
    val laundryshop: LaundryShop,

    @OneToMany
    val pickupItems: List<PickupItem>,

    @OneToOne
    val payment: Payment,

    @Enumerated(EnumType.STRING)
    var status: PickupStatus,

    @Nullable
    val content: String,
){
    @CreationTimestamp
    lateinit var createdAt: Timestamp

    @UpdateTimestamp
    lateinit var updateAt: Timestamp
}
