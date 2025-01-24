package org.washcode.washpang.domain.laundryshop.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.user.entity.User
import java.sql.Timestamp

@Entity
class LaundryShop (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    var user: User,
    var shopName: String,
    var businessNumber: String,
    var userName: String,
    var phone: String,
    var address: String,
    var nonOperatingDays: String,

    var latitude: Double,
    var longitude: Double,

    @CreationTimestamp
    var createdAt: Timestamp
)
