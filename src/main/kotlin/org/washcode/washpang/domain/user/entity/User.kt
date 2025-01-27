package org.washcode.washpang.domain.user.entity

import org.hibernate.annotations.*
import jakarta.persistence.*
import org.washcode.washpang.global.comm.enums.UserRole
import java.sql.Timestamp

@Entity
class User ( // 클래스 'User'에는 [public, protected] no-arg 생성자가 포함되어야 합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(unique = true)
    val kakao_id: Long,

    var name: String,
    var password: String,
    var baseAddress: String,
    var detailedAddress: String,
    var phone: String,
    var email: String,

    @Enumerated(EnumType.STRING)
    @ColumnDefault("USER")
    var role: UserRole,

    @CreationTimestamp
    val created_at: Timestamp
)