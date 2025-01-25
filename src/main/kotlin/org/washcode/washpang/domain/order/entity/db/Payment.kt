package org.washcode.washpang.domain.order.entity.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.global.comm.client.Dto.KakaoPayDto
//import org.washcode.washpang.domain.order.dto.KakaoPayApproveRes
//import org.washcode.washpang.domain.pickup.entity.Pickup
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
    val paymentDatetime: Timestamp,
    val amount: Int,
    val method: String,

    // 카카오페이 등 결제 데이터
    var aid: String? = null,
    var tid: String? = null,
    var paymentMethodType: String? = null,
    var createdAt: String? = null,
    var approvedAt: String? = null,
    var payload: String? = null
){

    // 카카오페이 결제 데이터 업데이트 메서드
    fun updateKakaoPayData(kakaoPayApproveRes: KakaoPayDto.KakaoPayApproveRes) {
        this.aid = kakaoPayApproveRes.aid
        this.tid = kakaoPayApproveRes.tid
        this.paymentMethodType = kakaoPayApproveRes.paymentMethodType
        this.createdAt = kakaoPayApproveRes.createdAt
        this.approvedAt = kakaoPayApproveRes.approvedAt
        this.payload = kakaoPayApproveRes.payload
    }
}