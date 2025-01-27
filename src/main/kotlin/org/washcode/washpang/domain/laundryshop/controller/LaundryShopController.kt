package org.washcode.washpang.domain.laundryshop.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.handledItems.dto.HandledItemsResDTO
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.service.HandledItemsService
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO.LaundryDetailResDTO
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO.ShopAddReqDTO
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.laundryshop.service.LaundryShopService
import org.washcode.washpang.global.comm.enums.LaundryCategory
import java.util.*

@RestController
@RequestMapping("/api/laundry")
class LaundryShopController(
    private val laundryShopService: LaundryShopService,
    private val handledItemsService: HandledItemsService
) {

    @GetMapping("/map")
    @Operation(summary = "지도로 목록 조회", description = "지도위에서 세탁소 위치 및 목록 확인")
    fun map(
        @RequestParam(value = "shopName", required = false) shopName: String?,
        @RequestParam(value = "userLat") userLat: Double,
        @RequestParam(value = "userLng") userLng: Double
    ): List<LaundryShop> {
        return if (shopName != null && !shopName.isEmpty()) {
            laundryShopService.getLaundryShops(shopName, userLat, userLng)
        } else {
            laundryShopService.getLaundryShops(userLat, userLng)
        }
    }

    //세탁소 정보가 이미 저장되어있는지 확인
    @GetMapping("/")
    fun checkLaundryExists( /*@AuthenticationPrincipal int id*/): LaundryDetailResDTO? {
        val id = 1
        println("checkLaundryExists: $id")

        return laundryShopService.getLaundryShopByUserId(id)
    }

    //세탁소 정보 저장
    @PostMapping("/")
    @Operation(summary = "세탁소 정보 저장", description = "세탁소 저장 API 입니다.")
    fun registerLaundry(
        @RequestBody to: ShopAddReqDTO
    /*, @AuthenticationPrincipal int id*/
    ): ResponseEntity<Map<String, Int>>
    {
        val id: Int = 1

        val laundryId = laundryShopService.registerLaundryShop(to, id);

        System.out.println(laundryId);

        // 성공 응답 반환
        return ResponseEntity.ok(mapOf("laundryId" to laundryId))
    }

    //가격표 저장
    @PostMapping("/handled-items")
    @Operation(summary = "가격표 저장", description = "가격표 저장 API 입니다.")
    fun setHandledItems(@RequestBody itemsList: List<HandledItemsResDTO>): List<HandledItems> {
        println("Received items list: $itemsList")

        return laundryShopService.upsertHandledItems(itemsList)
    }

    //세탁소 정보 수정
    @PutMapping("/")
    @Operation(summary = "세탁소 정보 수정", description = "세탁소 수정 API 입니다.")
    fun modifyLaundry(
    @RequestBody to: ShopAddReqDTO
    /*, @AuthenticationPrincipal int id*/
    ): ResponseEntity<Map<String, Int>>  {
        val id: Int = 1;

        val laundryId = laundryShopService.registerLaundryShop(to, id)

        System.out.println(laundryId)

        // 성공 응답 반환
        return ResponseEntity.ok().body(mapOf("laundryId" to laundryId))
    }

    //가격표 수정
    @PutMapping("/handled-items")
    @Operation(summary = "가격표 수정", description = "가격표 수정 API 입니다.")
    fun setHandledItemsModify(@RequestBody itemsList: List<HandledItemsResDTO>): List<HandledItems> {
        println("Received items list: $itemsList")

        return laundryShopService.upsertHandledItems(itemsList)
    }

    //세탁소 상세 조회
    @GetMapping("/{laundryId}")
    @Operation(summary = "세탁소 상세 조회", description = "세탁소 상세 조회 API 입니다.")
    fun getHandledItems(
        @PathVariable("laundryId") laundryId: Int /*@AuthenticationPrincipal int id*/
    ): MutableMap<String, Any> {
        val id = 1
        val handledItems = handledItemsService.getAllHandledItems(laundryId)

        val response: MutableMap<String, Any> = HashMap()
        response["id"] = id
        response["handledItems"] = handledItems

        return response
    }

    //카테고리별로 세탁소 list 조회
    @GetMapping("/category/{category}")
    @Operation(summary = "세탁소 카테고리별 조회", description = "세탁소 카테고리별 조회 API 입니다.")
    fun getLaundryShopsCategory(@PathVariable("category") category: String): List<Map<String, Any>> {
        val laundryCategory = LaundryCategory.valueOf(category.uppercase(Locale.getDefault()))

        val shops = laundryShopService.findLaundryShopsByCategory(laundryCategory)

        val result: MutableList<Map<String, Any>> = ArrayList()

        for (shop in shops) {
            val shopData: MutableMap<String, Any> = HashMap()
            shopData["shop"] = shop

            // 해당 세탁소의 가장 저렴한 항목 조회
            val handledItems = handledItemsService.getAllHandledItems(shop.id)
            val cheapestItem = handledItems.minBy { it.price }

            shopData["cheapestItem"] = cheapestItem

            result.add(shopData)
        }

        return result
    }
}
