package org.washcode.washpang.domain.laundryshop.dto

import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.global.comm.enums.LaundryCategory


data class LaundryDetailResDTO(
     var shopName: String?,
     var phone: String?,
     var address: String?,
     var nonOperatingDays: String?,
     var businessNumber: String?,
     var userName: String?,

     //var handledItems: List<HandledItems>?
     var handledItems: List<HandledItems>
){
     data class HandledItems(
          var id: Int?,
          //var laundryshop: LaundryShop,
          var itemName: String?,
          var category: LaundryCategory?,
          var price: Int?
     )
}
    // 세탁소 상세 보기
//    private val shopName: String? = null
//    private val phone: String? = null
//    private val address: String? = null
//    private val nonOperatingDays: String? = null
//    private val businessNumber: String? = null
//    private val userName: String? = null
//
//    private val handledItems: List<HandledItems>? = null

