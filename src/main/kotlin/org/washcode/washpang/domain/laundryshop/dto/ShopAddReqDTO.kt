package org.washcode.washpang.domain.laundryshop.dto

data class ShopAddReqDTO(
     var shopName: String?,
     var businessNumber: String?,
     var address: String?,
     var phone: String?,
     var nonOperatingDays: String?,
     var userName: String?,

     var latitude: Double,
     var longitude: Double,

     var userId: Int
)

//    // 세탁소 등록 (업체)
//    private var shopName: String? = null
//    private var businessNumber: String? = null
//    private var address: String? = null
//    private var phone: String? = null
//    private var nonOperatingDays: String? = null
//    private var userName: String?= null
//
//    private var latitude = 0.0
//    private var longitude = 0.0
//
//    private var user_id = 0

