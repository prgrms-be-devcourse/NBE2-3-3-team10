package org.washcode.washpang.domain.pickup.dto

import org.washcode.washpang.global.comm.enums.PickupStatus
import java.sql.Timestamp

class PickupDto private constructor() {

    data class DeliveryRes(
        val pickupId: Int,
        val shopName: String,
        val createdAt: Timestamp,
        val address: String,
        val phone: String,
        val content: String,
        val orderItems: List<OrderItemDTO>
    ) {
        data class OrderItemDTO(
            val itemName: String,
            val quantity: Int,
            val totalPrice: Int
        )
    }

    data class DetailRes(
        val pickupId: Int,
        val shopName: String,
        val createdAt: Timestamp,
        val address: String,
        val phone: String,
        val content: String,
        val orderItems: List<OrderItemDTO>,
        val paymentAmount: Int,
        val paymentMethod: String,
    ) {
        data class OrderItemDTO(
            val itemName: String,
            val quantity: Int,
            val totalPrice: Int
        )
    }

    data class Res(
        val pickupId: Int,
        val status: PickupStatus,
        val createdAt: Timestamp,
        val address: String,
        val content: String,
        val orderItems: List<OrderItemDTO>,
    ){
        data class OrderItemDTO(
            val itemName: String,
            val quantity: Int,
            val totalPrice: Int
        )
    }

    data class SalesSummery(
        val pickupId: Int,
        val status: PickupStatus,
        val createdAt: Timestamp,
        val address: String,
        val orderItems: List<OrderItemDTO>,
    ){
        data class OrderItemDTO(
            val itemName: String,
            val quantity: Int,
            val totalPrice: Int
        )
    }

    data class StateUpdateReq(
        val pickupId: Int,
        val status: String,
    )
}