package org.washcode.washpang.domain.order.dto

import org.washcode.washpang.domain.handledItems.dto.ItemInfoResDto
import java.sql.Timestamp


class OrderDto private constructor(){

    data class InfoRes (
        var name: String,
        var address: String,
        var shopName: String,
        var category: List<ItemInfoResDto>
    )

    data class ItemReq(
        var itemId: Int = 0,         // 항목 ID
        var quantity: Int = 0,       // 수량
        var totalPrice: Int = 0      // 총 가격
    )

    data class listRes(
        val pickupId: Int,
        val shopName: String,
        val status: String,
        val createdAt: String
    ) {
        var updateAt: String? = ""
    }

    data class OrderReq(
        var laundryshopId: Int,
        var content: String,
        var itemId: Int,
        var quantity: Int,
        var paymentMethod: String
    )

    data class OrderRes(
        var name: String,
        var address: String,
        var phone: String,
        var shopName: String,
        var content: String,
        var status: String,
        var createdAt: String,
        var updateAt: String,
        var paymentId: Int,
        var method: String,
        var amount: Int = 0,
        var paymentDatetime: Timestamp,
        var price: Int = 0,
        var orderItems: List<OrderItem>  // 빈 리스트로 초기화
    ) {
        // OrderItem 클래스 정의
        data class OrderItem(
            var itemName: String,
            var quantity: Int,
            var totalPrice: Int
        )
    }

    data class KakaoPayApproveRes (
        val aid: String,                // 요청 고유 번호
        val tid: String,                // 결제 고유 번호
        val paymentMethodType: String,   // 결제 수단, CARD 또는 MONEY 중 하나
        val createdAt: String,           // 결제 준비 요청 시각
        val approvedAt: String,          // 결제 승인 시각
        val payload: String
    )

    data class KakaoPayReq(
        val name: String,
        val totalPrice: Int,
        val quantity: Int,
        val paymentId: Int
    )

}