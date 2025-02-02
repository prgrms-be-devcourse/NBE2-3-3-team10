package org.washcode.washpang.domain.pickup.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.washcode.washpang.domain.pickup.dto.PickupDto
import org.washcode.washpang.domain.pickup.service.PickupService
import org.washcode.washpang.global.comm.enums.PickupStatus
import org.washcode.washpang.global.exception.ResponseResult
import java.util.Calendar

@RestController
@RequestMapping("/api/pickup")
class PickupController (
    private val pickupService : PickupService,
) {
    @GetMapping("/pickupId")
    fun getPickupDetail(@RequestParam("id") pickupId: Long): ResponseResult {
        val pickupDetail: PickupDto.Res = pickupService.getPickupDetail(pickupId)

        return ResponseResult(pickupDetail)
    }

    @GetMapping("/pickupList/userId")
    fun getPickupList(@AuthenticationPrincipal id: Int): ResponseResult {
        val pickupList: List<PickupDto.DetailRes> = pickupService.getPickupList(id.toLong())

        return ResponseResult(pickupList)
    }

    @PostMapping("/updateStatus")
    fun updateStatus(
        @RequestParam("pickupId") pickupId: Long,
        @RequestParam("status") statusStr: String
    ): ResponseResult {
        val newStatus: PickupStatus = PickupStatus.valueOf(statusStr)
        pickupService.updatePickupStatus(pickupId, newStatus)

        return ResponseResult(newStatus)
    }

    @GetMapping("/pickedUpList/userId")
    fun getPickedUpListByUserId(@AuthenticationPrincipal id: Int): ResponseResult {
        val pickedUpList: List<PickupDto.Res> = pickupService.getPickedupListAndUpdateStatus(id.toLong())

        return ResponseResult(pickedUpList)
    }

    @GetMapping("/pickupDelivery/userId")
    fun getDeliveryPickupListByUserId(@AuthenticationPrincipal id: Int): ResponseResult {
        val pickupList: List<PickupDto.DeliveryRes> = pickupService.getPickupDeliveryList(id.toLong())

        return ResponseResult(pickupList)
    }

    @GetMapping("/sales-summary/page")
    fun getSalesSummeryPage(@AuthenticationPrincipal id: Int): ResponseResult {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH] + 1

        val pickupList: List<PickupDto.SalesSummery> =
            pickupService.getPickupSalesSummery(id.toLong(), currentYear, currentMonth)

        return ResponseResult(pickupList)
    }

    @GetMapping("/sales-summary")
    fun getSalesSummary(
        @AuthenticationPrincipal id: Int,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int
    ): ResponseResult {
        System.out.println(pickupService.getPickupSalesSummery(id.toLong(), year, month))
        val pickupList: List<PickupDto.SalesSummery> = pickupService.getPickupSalesSummery(id.toLong(), year, month)

        return ResponseResult(pickupList)
    }

}