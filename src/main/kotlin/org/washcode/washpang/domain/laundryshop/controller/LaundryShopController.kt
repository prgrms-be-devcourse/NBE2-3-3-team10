package org.washcode.washpang.domain.laundryshop.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.handledItems.dto.HandledItemsResDTO
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.service.HandledItemsService
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO.LaundryDetailResDTO
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.laundryshop.service.LaundryShopService
import org.washcode.washpang.global.comm.enums.LaundryCategory
import java.util.*

@RestController
@RequestMapping("/api/laundry")
class LaundryShopController {
    @Autowired
    private val laundryShopService: LaundryShopService? = null

    @Autowired
    private val handledItemsService: HandledItemsService? = null


    @GetMapping("/map")
    fun map(
        @RequestParam(value = "shopName", required = false) shopName: String?,
        @RequestParam(value = "userLat") userLat: Double,
        @RequestParam(value = "userLng") userLng: Double
    ): List<LaundryShop> {
        return if (shopName != null && !shopName.isEmpty()) {
            laundryShopService!!.getLaundryShops(shopName, userLat, userLng)
        } else {
            laundryShopService!!.getLaundryShops(userLat, userLng)
        }
    }

    //세탁소 정보가 이미 저장되어있는지 확인
    @GetMapping("/")
    fun checkLaundryExists( /*@AuthenticationPrincipal int id*/): LaundryDetailResDTO? {
        val id = 1
        println("checkLaundryExists: $id")

        return laundryShopService!!.getLaundryShopByUserId(id)
    }

    //세탁소 정보 저장
    //    @PostMapping("/")
    //    public ResponseEntity<?> registerLaundry(@RequestBody LaundryDTO.ShopAddReqDTO to, /*@AuthenticationPrincipal int id*/) {
    //        int id = 1;
    //
    //        int laundryId = laundryShopService.registerLaundryShop(to, id);
    //
    //        System.out.println(laundryId);
    //
    //        // 성공 응답 반환
    //        return ResponseEntity.ok().body(Map.of("laundryId", laundryId));
    //    }
    //가격표 저장
    @PostMapping("/handled-items")
    fun setHandledItems(@RequestBody itemsList: List<HandledItemsResDTO>): List<HandledItems> {
        println("Received items list: $itemsList")

        return laundryShopService!!.setHandledItems(itemsList)
    }

    //세탁소 정보 수정
    //    @PutMapping("/")
    //    public ResponseEntity<?> modifyLaundry(@RequestBody LaundryDTO.ShopAddReqDTO to, /*@AuthenticationPrincipal int id*/) {
    //        int id = 1;
    //        //System.out.println(to.getUserName());
    //
    //        int laundryId = laundryShopService.registerLaundryShop(to, id);
    //
    //        System.out.println(laundryId);
    //        // 성공 응답 반환
    //        return ResponseEntity.ok().body(Map.of("laundryId", laundryId));
    //    }
    //가격표 수정
    @PutMapping("/handled-items")
    fun setHandledItemsModify(@RequestBody itemsList: List<HandledItemsResDTO>): List<HandledItems> {
        println("Received items list: $itemsList")

        return laundryShopService!!.setHandledItems(itemsList)
    }

    //세탁소 카테고리 가져오기
    @GetMapping("/{laundryId}")
    fun getHandledItems(
        @PathVariable("laundryId") laundryId: Long /*@AuthenticationPrincipal int id*/
    ): Map<String, Any> {
        val id = 1
        val handledItems = handledItemsService!!.getAllHandledItems(laundryId)

        val response: MutableMap<String, Any> = HashMap()
        response["id"] = id
        response["handledItems"] = handledItems

        return response
    }

    //카테고리별로 세탁소 list 조회
    @GetMapping("/category/{category}")
    fun getLaundryShopsCategory(@PathVariable("category") category: String): List<Map<String, Any?>> {
        val laundryCategory = LaundryCategory.valueOf(category.uppercase(Locale.getDefault()))

        val shops = laundryShopService!!.findLaundryShopsByCategory(laundryCategory)


        val result: MutableList<Map<String, Any?>> = ArrayList()

        for (shop in shops) {
            val shopData: MutableMap<String, Any?> = HashMap()
            shopData["shop"] = shop

            // 해당 세탁소의 가장 저렴한 항목 조회
            val handledItems = handledItemsService!!.getAllHandledItems(shop.id.toLong())
            val cheapestItem = handledItems.stream()
                .min(Comparator.comparing(HandledItems::price))
                .orElse(null)

            shopData["cheapestItem"] = cheapestItem

            result.add(shopData)
        }

        return result
    }
}
