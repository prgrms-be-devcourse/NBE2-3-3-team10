package org.washcode.washpang.domain.order.dto

import org.washcode.washpang.domain.handledItems.dto.ItemInfoResDTO
import java.sql.Timestamp


class OrderDto private constructor(){

    data class InfoRes (
        val name: String,
        val baseAddress: String,        //주소
        val detailedAddress: String,    //상세주소
        val shopName: String,
        val category: List<ItemInfoResDTO>? = emptyList()  // 빈 리스트로 초기화

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
        var amount: Int,
        var paymentDatetime: Timestamp? = Timestamp.valueOf("1925-01-01 00:00:00"),
        var price: Int,
        var orderItems: MutableList<OrderItem> = mutableListOf()

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