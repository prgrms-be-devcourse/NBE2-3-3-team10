package org.washcode.washpang.domain.laundryshop.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.handledItems.dto.HandledItemsResDTO
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.repository.HandledItemsRepository
import org.washcode.washpang.domain.handledItems.service.HandledItemsService

import org.washcode.washpang.domain.laundryshop.dto.LaundryDetailResDTO
import org.washcode.washpang.domain.laundryshop.dto.ShopAddReqDTO
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

    @Autowired
    private val handledItemsRepository: HandledItemsRepository? = null

    @GetMapping("/map")
    fun map(
        @RequestParam(value = "shopName", required = false) shopName: String?,
        @RequestParam(value = "userLat") userLat: Double,
        @RequestParam(value = "userLng") userLng: Double
    ): List<LaundryShop?> {
        return if (shopName != null && !shopName.isEmpty()) {
            laundryShopService!!.getLaundryShops(shopName, userLat, userLng)
        } else {
            laundryShopService!!.getLaundryShops(userLat, userLng)
        }
    }

    //세탁소 정보가 이미 저장되어있는지 확인
    @GetMapping("/")
    fun checkLaundryExists(id: Int): LaundryDetailResDTO? {
        println("checkLaundryExists: $id")

        return laundryShopService!!.getLaundryShopByUserId(id)
    }

    //세탁소 정보 저장
    @PostMapping("/")
    fun registerLaundry(@RequestBody to: ShopAddReqDTO, id: Int): ResponseEntity<*> {
        //System.out.println(to.userName);

        val laundryId = laundryShopService!!.registerLaundryShop(to, id)

        // 성공 응답 반환
        return ResponseEntity.ok().body(java.util.Map.of("laundryId", laundryId))
    }

    //가격표 저장
    @PostMapping("/handled-items")
    fun setHandledItems(@RequestBody itemsList: List<HandledItemsResDTO>): List<HandledItems> {
        println("Received items list: $itemsList")

        return laundryShopService!!.setHandledItems(itemsList)
    }

    //세탁소 정보 수정
    @PutMapping("/")
    fun modifyLaundry(@RequestBody to: ShopAddReqDTO, id: Int): ResponseEntity<*> {
        //System.out.println(to.userName);

        val laundryId = laundryShopService!!.registerLaundryShop(to, id)

        // 성공 응답 반환
        return ResponseEntity.ok().body(java.util.Map.of("laundryId", laundryId))
    }

    //가격표 수정
    @PutMapping("/handled-items")
    fun setHandledItemsModify(@RequestBody itemsList: List<HandledItemsResDTO>): List<HandledItems> {
        println("Received items list: $itemsList")

        return laundryShopService!!.setHandledItems(itemsList)
    }

    //세탁소 카테고리 가져오기
    @GetMapping("/{laundryId}")
    fun getHandledItems(
        @PathVariable("laundryId") laundryId: Long,
        id: Int
    ): Map<String, Any> {
        // handledItemsService에서 반환되는 리스트가 List<HandledItems?>?일 수 있기 때문에 null 처리 및 타입 변환
        val handledItems = handledItemsService!!.getAllHandledItems(laundryId)?.filterNotNull() ?: emptyList<HandledItems>()

        val response: MutableMap<String, Any> = HashMap()
        response["id"] = id
        response["handledItems"] = handledItems

        return response
    }

    //카테고리별로 세탁소 list 조회
    @GetMapping("/category/{category}")
    fun getLaundryShopsCategory(@PathVariable("category") category: String): MutableList<Map<String, Any?>> {
        val laundryCategory = LaundryCategory.valueOf(category.uppercase(Locale.getDefault()))

        val shops = laundryShopService!!.findLaundryShopsByCategory(laundryCategory)


        val result: MutableList<Map<String, Any?>> = ArrayList()

        for (shop in shops!!) {
            val shopData: MutableMap<String, Any?> = HashMap()
            shopData["shop"] = shop

            // 해당 세탁소의 가장 저렴한 항목 조회
            val handledItems = handledItemsRepository!!.findByLaundryshopId(shop!!.id.toLong())
            val cheapestItem = handledItems!!.stream()
                .min(Comparator.comparing { obj: HandledItems? -> obj!!.price })
                .orElse(null)

            shopData["cheapestItem"] = cheapestItem

            result.add(shopData)
        }

        return result
    }
}
