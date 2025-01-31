package org.washcode.washpang.domain.pickup.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
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

    @Enumerated(EnumType.STRING)
    var status: PickupStatus,

    var content: String?,
){
    @CreationTimestamp
    lateinit var createdAt: Timestamp

    @UpdateTimestamp
    lateinit var updateAt: Timestamp
}
