package org.washcode.washpang.domain.order.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoPayApproveRes(
    var aid: String? = "",                    // 요청 고유 번호
    var tid: String? = "",                    // 결제 고유 번호

    @JsonProperty("paymentMethodType")
    var paymentMethodType: String? = "",      // 결제 수단, CARD 또는 MONEY 중 하나

    @JsonProperty("createdAt")
    var createdAt: String? = "",              // 결제 준비 요청 시각

    @JsonProperty("approvedAt")
    var approvedAt: String? = "",             // 결제 승인 시각

    var payload: String? = ""                 // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
)
