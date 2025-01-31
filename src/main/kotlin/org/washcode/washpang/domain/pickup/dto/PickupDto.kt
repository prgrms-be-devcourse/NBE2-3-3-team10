package org.washcode.washpang.domain.pickup.dto

import org.washcode.washpang.global.comm.enums.PickupStatus
import java.sql.Timestamp

class PickupDto private constructor() {

    data class OrderItem(
        val itemName: String,
        val quantity: Int,
        val totalPrice: Int
    )

    data class DeliveryRes(
        val pickupId: Int,
        val shopName: String,
        val createdAt: Timestamp,
        val baseAddress: String,
        val phone: String,
        val content: String,
        val orderItems: List<OrderItem>
    )

    data class DetailRes(
        val pickupId: Int,
        val shopName: String,
        val createdAt: Timestamp,
        val baseAddress: String,
        val phone: String,
        val content: String,
        val orderItems: List<OrderItem>,
        val paymentAmount: Int,
        val paymentMethod: String,
    )

    data class Res(
        val pickupId: Int,
        val status: PickupStatus,
        val createdAt: Timestamp,
        val baseAddress: String,
        val content: String,
        val orderItems: List<OrderItem>,
    )

    data class SalesSummery(
        val pickupId: Int,
        val status: PickupStatus,
        val createdAt: Timestamp,
        val baseAddress: String,
        val orderItems: List<OrderItem>,
    )

    data class StateUpdateReq(
        val pickupId: Int,
        val status: String,
    )
}