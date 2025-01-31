package org.washcode.washpang.domain.order.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.global.domain.kakaopay.dto.KakaoPayDto
import java.sql.Timestamp

@Entity
class Payment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int, // 결제 id

    @OneToOne
    @JoinColumn(name = "pickupId")
    val pickup: Pickup, // 요청 id

    @CreationTimestamp
    var paymentDatetime: Timestamp,
    var amount: Int,
    var method: String,
){
    // 카카오페이 등 결제 데이터
    private var aid: String? = null
    private var tid: String? = null
    private var paymentMethodType: String? = null
    private var createdAt: String? = null
    private var approvedAt: String? = null

    // 카카오페이 결제 데이터 업데이트 메서드
    fun updateKakaoPayData(kakaoPayApproveRes: KakaoPayDto.ApproveRes) {
        this.aid = kakaoPayApproveRes.aid
        this.tid = kakaoPayApproveRes.tid
        this.paymentMethodType = kakaoPayApproveRes.paymentMethodType
        this.createdAt = kakaoPayApproveRes.createdAt
        this.approvedAt = kakaoPayApproveRes.approvedAt
    }
}