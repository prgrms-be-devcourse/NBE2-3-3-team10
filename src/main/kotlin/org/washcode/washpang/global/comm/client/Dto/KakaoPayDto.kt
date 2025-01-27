package org.washcode.washpang.global.comm.client.Dto

class KakaoPayDto private constructor() {
    data class KakaoPayApproveRes (
        val aid: String,                // 요청 고유 번호
        val tid: String,                // 결제 고유 번호
        val paymentMethodType: String,   // 결제 수단, CARD 또는 MONEY 중 하나
        val createdAt: String,           // 결제 준비 요청 시각
        val approvedAt: String,          // 결제 승인 시각
        val payload: String
    )
}