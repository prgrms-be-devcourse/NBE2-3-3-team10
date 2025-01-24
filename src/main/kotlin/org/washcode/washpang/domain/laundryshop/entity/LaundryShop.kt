package org.washcode.washpang.domain.laundryshop.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.user.entity.User
import java.sql.Timestamp

@Entity
class LaundryShop (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    var user: User?,

    var shopName: String?,
    var businessNumber: String?,
    var userName: String?,
    var phone: String?,
    var address: String?,
    var nonOperatingDays: String?,

    var latitude: Double,
    var longitude: Double,

    @CreationTimestamp
    var createdAt: Timestamp?
)
