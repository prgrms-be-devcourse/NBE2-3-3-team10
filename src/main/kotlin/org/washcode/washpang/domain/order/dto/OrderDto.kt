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
        val paymentDatetime: Timestamp? = Timestamp.valueOf("1925-01-01 00:00:00"),
        val price: Int,
        var orderItems: List<OrderItem>
    )

    data class OrderItem(
        val itemName: String,
        val quantity: Int,
        val totalPrice: Int
    )
}