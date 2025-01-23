package org.washcode.washpang.domain.order.entity.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.order.dto.KakaoPayApproveRes
import java.sql.Timestamp

@Entity
class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0 // 결제 id

    @OneToOne
    @JoinColumn(name = "pickupId")
    var pickup: Pickup? = null // 요청 id

    @CreationTimestamp
    var paymentDatetime: Timestamp? = null
    var amount: Int = 0
    var method: String? = ""

    // 카카오페이 등 결제 데이터
    var aid: String? = ""
    var tid: String? = ""
    var paymentMethodType: String? = ""
    var createdAt: String? = ""
    var approvedAt: String? = ""
    var payload: String? = ""

    // 간편결제 후, 데이터 삽입
    fun setKakaoPayData(kakaoPayApproveRes: KakaoPayApproveRes) {
        this.aid = kakaoPayApproveRes.aid
        this.tid = kakaoPayApproveRes.tid
        this.paymentMethodType = kakaoPayApproveRes.paymentMethodType
        this.createdAt = kakaoPayApproveRes.createdAt
        this.approvedAt = kakaoPayApproveRes.approvedAt
        this.payload = kakaoPayApproveRes.payload
    }
}
