package org.washcode.washpang.domain.inquiry.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.user.entity.User
import java.sql.Timestamp

@Entity
class Inquiry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,  // 기본값 설정

    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "laundryId")
    val laundryShop: LaundryShop,

    var title: String,
    var inquiryContent: String,
    var replyContent: String? = null,
)
{
    @CreationTimestamp
    lateinit var createdAt: Timestamp
}