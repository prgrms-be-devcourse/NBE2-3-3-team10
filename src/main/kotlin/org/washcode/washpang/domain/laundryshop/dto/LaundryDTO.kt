package org.washcode.washpang.domain.laundryshop.dto

import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.global.comm.enums.LaundryCategory

class LaundryDTO private constructor() {
    data class LaundryDetailResDTO(
        val shopName: String?,
        val phone: String?,
        val address: String?,
        val nonOperatingDays: String?,
        val businessNumber: String?,
        val userName: String?,

        //var handledItems: List<HandledItems>?
        val handledItems: List<HandledItems>
    ){
        data class HandledItems(
            val id: Int?,
            val laundryshop: LaundryShop?,
            val itemName: String?,
            val category: LaundryCategory?,
            val price: Int?
        )
    }

    data class ShopAddReqDTO(
        val shopName: String?,
        val businessNumber: String?,
        val address: String?,
        val phone: String?,
        val nonOperatingDays: String?,
        val userName: String?,

        val latitude: Double,
        val longitude: Double,

        val userId: Int
    )
}