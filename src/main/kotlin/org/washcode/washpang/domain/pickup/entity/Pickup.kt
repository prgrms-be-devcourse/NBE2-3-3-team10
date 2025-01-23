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
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
data class Pickup(
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

    @Nullable
    val content: String,
){
    @CreationTimestamp
    lateinit var createdAt: Timestamp

    @UpdateTimestamp
    lateinit var updateAt: Timestamp
}
