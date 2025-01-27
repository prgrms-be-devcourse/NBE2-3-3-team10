package org.washcode.washpang.global.domain.kakaopay.dto

import com.fasterxml.jackson.annotation.JsonProperty

class KakaoPayDto private constructor() {
    data class ApproveRes (
        val aid: String,                 // 요청 고유 번호
        val tid: String,                 // 결제 고유 번호
        val paymentMethodType: String,   // 결제 수단, CARD 또는 MONEY 중 하나
        val createdAt: String,           // 결제 준비 요청 시각
        val approvedAt: String,          // 결제 승인 시각
        val payload: String
    )

    data class ReqDto (
        val name: String,
        val totalPrice: Int,
        val quantity: Int,
        val paymentId: String
    )

    // 카카오 페이 준비
    data class ReadyReq (
        val cid: String,
        val cid_secret: String,
        val partner_order_id: String,
        val partner_user_id: String,
        val item_name: String,
        val quantity: Int,
        val total_amount: Int,
    ) {
        val tax_free_amount: Int = 0
        val approval_url = "http://localhost:8080/customer/order/completed"
        val cancel_url = "http://localhost:8080/customer/order/cancel"
        val fail_url = "http://localhost:8080/customer/order/fail"

        override fun toString(): String {
            return "cid=$cid&" +
                    "cid_secret=$cid_secret&" +
                    "partner_order_id=$partner_order_id&" +
                    "partner_user_id=$partner_user_id&" +
                    "item_name=$item_name&" +
                    "quantity=$quantity&" +
                    "total_amount=$total_amount&" +
                    "tax_free_amount=$tax_free_amount&" +
                    "approval_url=$approval_url&" +
                    "cancel_url=$cancel_url&" +
                    "fail_url=$fail_url"
        }
    }

    data class ReadyRes (
        val tid: String,
        val tms_result: String,
        val created_at: String,
        val next_redirect_pc_url: String,
        val next_redirect_mobile_url: String,
        val next_redirect_app_url: String,
        val android_app_scheme: String,
        val ios_app_scheme: String
    )
}