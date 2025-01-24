package org.washcode.washpang.domain.user.entity

import org.hibernate.annotations.*
import jakarta.persistence.*
import java.security.Timestamp

@Entity
class User ( // 클래스 'User'에는 [public, protected] no-arg 생성자가 포함되어야 합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(unique = true)
    val kakao_id: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val password: String,

    @Column(unique = false)
    val baseAddress: String,

    @Column(unique = false)
    val detailedAddress: String,

    @Column(nullable = false)
    val phone: String,

    @Column(nullable = false)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("USER")
    val role: EnumType,

    @CreationTimestamp
    val created_at: Timestamp
)