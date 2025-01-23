package org.washcode.washpang.domain.order.dto

data class KakaoPayReqDTO(
    val name: String? = "",
    val totalPrice: Int = 0,
    val quantity: Int = 0,
    val paymentId: Int = 0
)

