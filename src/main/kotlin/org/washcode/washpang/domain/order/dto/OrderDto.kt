package org.washcode.washpang.domain.order.dto

import org.washcode.washpang.domain.handledItems.dto.ItemInfoResDto
import java.sql.Timestamp


class OrderDto private constructor(){

    data class InfoRes (
        val name: String,
        val address: String,
        val shopName: String,
        //val category: List<ItemInfoResDTO>? = emptyList()  // 빈 리스트로 초기화

    )

    data class ItemReq(
        val itemId: Int,         // 항목 ID
        val quantity: Int,       // 수량
        val totalPrice: Int      // 총 가격
    )

    data class listRes(
        val pickupId: Int,
        val shopName: String,
        val status: String,
        val createdAt: String
    ) {
        lateinit var updateAt: String
    }

    data class OrderReq(

        val laundryshopId: Int,
        val content: String,
        val itemId: Int,
        val quantity: Int,
        val paymentMethod: String
    )

    data class OrderRes(
        val name: String,
        val address: String,
        val phone: String,
        val shopName: String,
        val content: String,
        val status: String,
        val createdAt: String,
        val updateAt: String,
        val paymentId: Int,
        val method: String,
        val amount: Int,
        val paymentDatetime: Timestamp? = Timestamp.valueOf("1925-01-01 00:00:00"), //
        val price: Int,
        val orderItems: List<OrderItem>? = emptyList()  // 빈 리스트로 초기화

    ) {
        // OrderItem 클래스 정의
        data class OrderItem(
            val itemName: String,
            val quantity: Int,
            val totalPrice: Int
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