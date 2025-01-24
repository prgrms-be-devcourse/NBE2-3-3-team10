package org.washcode.washpang.domain.inquiry.entity

import jakarta.persistence.*
import org.washcode.washpang.domain.user.entity.User
import java.sql.Timestamp

@Entity
class Inquiry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,  // 기본값 설정

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val inquiry_content: String? = null,
    val reply_content: String? = null,
    val created_at: Timestamp? = null
)