package org.washcode.washpang.domain.laundryshop.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.washcode.washpang.domain.handledItems.dto.HandledItemsResDTO
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.repository.HandledItemsRepository
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO.LaundryDetailResDTO
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO.ShopAddReqDTO
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.domain.user.entity.User
import org.washcode.washpang.global.comm.enums.LaundryCategory
import java.sql.Timestamp
import java.util.stream.Collectors
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
class LaundryShopService {
    @Autowired
    private val laundryShopRepository: LaundryShopRepository? = null

//    @Autowired
//    private val userRepository: UserRepository? = null

    @Autowired
    private val handledItemsRepository: HandledItemsRepository? = null

    fun getLaundryById(id: Long): LaundryShop {
        return laundryShopRepository!!.findById(id)
            .orElseThrow { RuntimeException("LaundryShop not found") }
    }

    fun getLaundryShops(userLat: Double, userLng: Double): List<LaundryShop> {
        val shops = laundryShopRepository!!.findAll()
        return sortByDistance(shops, userLat, userLng)
    }

    // 검색된 세탁소 리스트 거리순 정렬
    fun getLaundryShops(shopName: String, userLat: Double, userLng: Double): List<LaundryShop> {
        val shops = laundryShopRepository!!.findByShopNameContaining(shopName)
        return sortByDistance(shops, userLat, userLng)
    }

    // 거리 계산 및 정렬
    private fun sortByDistance(shops: List<LaundryShop>, userLat: Double, userLng: Double): List<LaundryShop> {
        return shops.stream()
            .sorted { shop1: LaundryShop, shop2: LaundryShop ->
                val distance1 = calculateDistance(
                    userLat, userLng,
                    shop1.latitude!!,
                    shop1.longitude!!
                )
                val distance2 = calculateDistance(userLat, userLng, shop2.latitude!!, shop2.longitude!!)
                java.lang.Double.compare(distance1, distance2)
            }
            .collect(Collectors.toList())
    }

    // Haversine 공식을 사용하여 거리 계산
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // 지구 반지름 (km)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (sin(dLat / 2) * sin(dLat / 2)
                + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // 거리 반환 (km)
    }

    //세탁소 상세정보 조회
    //세탁소 id로 세탁소 정보 찾기
    fun getLaundryShopById(id: Int): LaundryDetailResDTO {
        val laundryShop = laundryShopRepository!!.findById(id.toLong())
            .orElseThrow { RuntimeException("LaundryShop not found") }

        println("LaundryDetailResDTO: " + laundryShop.id)

        val handledItems = handledItemsRepository!!.findByLaundryshopId(laundryShop.id.toLong())
            .map {entity ->
                LaundryDetailResDTO.HandledItems(
                    id = entity.id,
                    itemName = entity.itemName,
                    laundryshop = entity.laundryshop,
                    category = entity.category,
                    price = entity.price
                )
            }

        val to = LaundryDetailResDTO(
            shopName = laundryShop.shopName,
            phone = laundryShop.phone,
            address = laundryShop.address,
            nonOperatingDays = laundryShop.nonOperatingDays,
            businessNumber = laundryShop.businessNumber,
            userName = laundryShop.userName,

            handledItems = handledItems
        )

        return to
    }

    //user_id로 세탁소 정보 찾기
    fun getLaundryShopByUserId(id: Int): LaundryDetailResDTO? {
        val laundryShop = laundryShopRepository!!.findByUserId(id).orElse(null) ?: return null
        println("LaundryDetailResDTO: " + laundryShop.id)

        val handledItems = handledItemsRepository!!.findByLaundryshopId(laundryShop.id.toLong())
            .map {entity ->
                LaundryDetailResDTO.HandledItems(
                    id = entity.id,
                    itemName = entity.itemName,
                    laundryshop = entity.laundryshop,
                    category = entity.category,
                    price = entity.price
                )
            }
        val to = LaundryDetailResDTO(
            shopName = laundryShop.shopName,
            phone = laundryShop.phone,
            address = laundryShop.address,
            nonOperatingDays = laundryShop.nonOperatingDays,
            businessNumber = laundryShop.businessNumber,
            userName = laundryShop.userName,

            handledItems = handledItems
        )

        return to
    }

    //카테고리로 세탁소 정보 찾기
    fun findLaundryShopsByCategory(category: LaundryCategory): List<LaundryShop> {
        // HandledItems에서 카테고리에 맞는 세탁소 ID 리스트 가져오기
        val shopIds = handledItemsRepository!!.findLaundryShopIdsByCategory(category)

        // 세탁소 정보 가져오기
        return laundryShopRepository!!.findByIdIn(shopIds)
    }

//    //세탁소 저장하기
//    fun registerLaundryShop(to: ShopAddReqDTO, id: Int): Int {
//        val user: User = userRepository.findById(id).orElse(null)
//        val shop = laundryShopRepository!!.findByUserId(id)
//            .orElseGet { LaundryShop() }
//
//        shop.user = user
//        shop.shopName = to.shopName
//        shop.businessNumber = to.businessNumber
//        shop.userName = to.userName
//        shop.address = to.address
//        shop.phone = to.phone
//        shop.nonOperatingDays = to.nonOperatingDays
//        shop.latitude = to.latitude
//        shop.longitude = to.longitude
//        shop.createdAt = Timestamp(System.currentTimeMillis())
//
//        val savedShop = laundryShopRepository.save(shop)
//
//        return savedShop.id
//    }


    //가격표 정보 등록 및 수정
    fun setHandledItems(items: List<HandledItemsResDTO>): List<HandledItems> {
        val laundryId = items[0].laundryId.toLong()
        val laundryShop = laundryShopRepository!!.findById(laundryId)
            .orElseThrow {
                IllegalArgumentException(
                    "LaundryShop not found with ID: $laundryId"
                )
            }


        //laundry_id로 이미 저장되어있는 가격표가 있다면 불러옴
        var handledItemsList = handledItemsRepository!!.findByLaundryshopId(laundryId).toMutableList()


        //없으면 새로 생성
        if (handledItemsList == null) {
            handledItemsList = ArrayList()
        }

        val toBeDeletedItems = handledItemsList.stream()
            .filter { existingItem: HandledItems ->
                !items.stream()
                    .anyMatch { item: HandledItemsResDTO ->
                        item.itemName == existingItem.itemName &&
                                item.category == existingItem.category
                    }
            }
            .collect(Collectors.toList())

        // 삭제할 항목들 삭제
        for (itemToDelete in toBeDeletedItems) {
            handledItemsRepository.delete(itemToDelete) // 해당 항목 삭제
            handledItemsList.remove(itemToDelete) // 로컬 리스트에서 삭제된 항목 제거
        }

        for ((itemName, category, price) in items) {
            // 기존 항목이 있는지 찾아보기
            val existingItemOptional = handledItemsList.stream()
                .filter { existingItem: HandledItems ->
                    existingItem.itemName == itemName &&
                            existingItem.category == category
                }
                .findFirst()

            val handledItem: HandledItems

            if (existingItemOptional.isPresent) {
                // 기존 항목이 있다면 업데이트
                handledItem = existingItemOptional.get()

                handledItem.itemName = itemName
                handledItem.category = category
                handledItem.price = price
            } else {
                // 기존 항목이 없다면 새로 생성
                handledItem = HandledItems()
                handledItem.laundryshop = laundryShop
                handledItem.itemName = itemName
                handledItem.category = category
                handledItem.price = price

                handledItemsList.add(handledItem) // 새 항목 추가
            }

            // 저장
            handledItemsRepository.save(handledItem)
        }

        // 전체 항목 저장
        return handledItemsRepository.saveAll(handledItemsList)
    }
}