package org.washcode.washpang.domain.laundryshop.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.washcode.washpang.domain.user.entity.User
import java.sql.Timestamp

@Entity
class LaundryShop (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @OneToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    val user: User,
    val shopName: String,
    val businessNumber: String,
    val userName: String,
    val phone: String,
    val address: String,
    val nonOperatingDays: String,

    val latitude: Double,
    val longitude: Double,
//
//    @CreationTimestamp
//    var createdAt: Timestamp
)
{
    @CreationTimestamp
    lateinit var createdAt: Timestamp
}