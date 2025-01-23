package org.washcode.washpang.domain.pickup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.pickup.dto.PickupDTO
import java.util.*

@RestController
@RequestMapping("/api/pickup")
class PickupController (
    private val pickupService : PickupService,
    private val reviewService : reviewService,
) {
    @GetMapping("/pickupId")
    fun getPickupDetail(@RequestParam("id") pickupId: Long): ResponseEntity<PickupDTO.Res> {
        val pickupDetail: PickupDTO.Res = pickupService.getPickupDetail(pickupId)

        return ResponseEntity.ok<PickupDTO.Res>(pickupDetail)
    }

    @GetMapping("/pickupList/userId")
    fun getPickupList(@AuthenticationPrincipal id: Int): ResponseEntity<List<PickupDTO.DetailRes>> {
        val pickupList: List<PickupDTO.DetailRes> = pickupService.getPickupList(id.toLong())

        return ResponseEntity.ok<List<PickupDTO.DetailRes>>(pickupList)
    }

    @PostMapping("/updateStatus")
    fun updateStatus(
        @RequestParam("pickupId") pickupId: Long,
        @RequestParam("status") statusStr: String
    ): ResponseEntity<Void> {
        val newStatus: PickupStatus = PickupStatus.valueOf(statusStr)
        pickupService.updatePickupStatus(pickupId, newStatus)

        return ResponseEntity.ok().build()
    }

    @GetMapping("/pickedUpList/userId")
    fun getPickedUpListByUserId(@AuthenticationPrincipal id: Int): ResponseEntity<List<PickupDTO.Res>> {
        val pickedUpList: List<PickupDTO.Res> = pickupService.getPickedupListAndUpdateStatus(id.toLong())

        return ResponseEntity.ok<List<PickupDTO.Res>>(pickedUpList)
    }

    @GetMapping("/pickupDelivery/userId")
    fun getDeliveryPickupListByUserId(@AuthenticationPrincipal id: Int): ResponseEntity<List<PickupDTO.DeliveryRes>> {
        val pickupList: List<PickupDTO.DeliveryRes> = pickupService.getPickupDeliveryList(id.toLong())

        return ResponseEntity.ok<List<PickupDTO.DeliveryRes>>(pickupList)
    }

    @GetMapping("/sales-summary/page")
    fun getSalesSummeryPage(@AuthenticationPrincipal id: Int): ResponseEntity<List<PickupDTO.SalesSummery>> {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH] + 1

        val pickupList: List<PickupDTO.SalesSummery> =
            pickupService.getPickupSalesSummery(id.toLong(), currentYear, currentMonth)

        return ResponseEntity.ok<List<PickupDTO.SalesSummery>>(pickupList)
    }

    @GetMapping("/sales-summary")
    fun getSalesSummary(
        @AuthenticationPrincipal id: Int,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int
    ): ResponseEntity<List<PickupDTO.SalesSummery>> {
        System.out.println(pickupService.getPickupSalesSummery(id.toLong(), year, month))
        val pickupList: List<PickupDTO.SalesSummery> = pickupService.getPickupSalesSummery(id.toLong(), year, month)

        return ResponseEntity.ok<List<PickupDTO.SalesSummery>>(pickupList)
    }

    @GetMapping("/shopReview")
    fun getReview(@AuthenticationPrincipal id: Int): ResponseEntity<List<ReviewResDTO>> {
        val reviewList: List<ReviewResDTO> = reviewService.getReviewList(1)

        return ResponseEntity.ok<List<ReviewResDTO>>(reviewList)
    }

}