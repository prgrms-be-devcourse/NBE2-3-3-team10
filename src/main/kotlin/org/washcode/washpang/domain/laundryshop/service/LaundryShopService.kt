package org.washcode.washpang.domain.laundryshop.service

import org.springframework.stereotype.Service
import org.washcode.washpang.domain.handledItems.dto.HandledItemsResDTO
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.repository.HandledItemsRepository
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO.LaundryDetailResDTO
import org.washcode.washpang.domain.laundryshop.dto.LaundryDTO.ShopAddReqDTO
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.global.comm.enums.LaundryCategory
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.stream.Collectors
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
class LaundryShopService(

    private val laundryShopRepository: LaundryShopRepository,
//    private val userRepository: UserRepository,
    private val handledItemsRepository: HandledItemsRepository,
) {
//    fun getLaundryById(id: Long): LaundryShop {
//        return laundryShopRepository.findById(id)
//            .orElseThrow { RuntimeException("LaundryShop not found") }
//    }

    fun getLaundryShops(userLat: Double, userLng: Double): List<LaundryShop> {
        val shops = laundryShopRepository.findAll()
        return sortByDistance(shops, userLat, userLng)
    }

    // 검색된 세탁소 리스트 거리순 정렬
    fun getLaundryShops(shopName: String, userLat: Double, userLng: Double): List<LaundryShop> {
        val shops = laundryShopRepository.findByShopNameContaining(shopName)
//        return sortByDistance(shops, userLat, userLng)

        return shops.sortedBy { shop ->
            calculateDistance(userLat, userLng, shop.latitude, shop.longitude)
        }
    }

    // 거리 계산 및 정렬
    private fun sortByDistance(shops: List<LaundryShop>, userLat: Double, userLng: Double): List<LaundryShop> {
        return shops.stream()
            .sorted { shop1: LaundryShop, shop2: LaundryShop ->
                val distance1 = calculateDistance(
                    userLat, userLng,
                    shop1.latitude,
                    shop1.longitude
                )
                val distance2 = calculateDistance(userLat, userLng, shop2.latitude, shop2.longitude)
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
        val laundryShop = laundryShopRepository.findById(id)

        //println("LaundryDetailResDTO: " + laundryShop.id)

        val handledItems = handledItemsRepository.findByLaundryshopId(laundryShop.id)
            .map {entity ->
                LaundryDetailResDTO.HandledItems(
                    id = entity.id,
                    itemName = entity.itemName,
                    laundryshop = entity.laundryshop,
                    category = entity.category,
                    price = entity.price
                )
            }

        val dto = LaundryDetailResDTO(
            shopName = laundryShop.shopName,
            phone = laundryShop.phone,
            address = laundryShop.address,
            nonOperatingDays = laundryShop.nonOperatingDays,
            businessNumber = laundryShop.businessNumber,
            userName = laundryShop.userName,

            handledItems = handledItems
        )

        return dto
    }

    //user_id로 세탁소 정보 찾기
    fun getLaundryShopByUserId(id: Int): LaundryDetailResDTO? {
        val laundryShop = laundryShopRepository.findByUserId(id)?: return null
        println("LaundryDetailResDTO: " + laundryShop.id)

        val handledItems = handledItemsRepository.findByLaundryshopId(laundryShop.id)
            .map {entity ->
                LaundryDetailResDTO.HandledItems(
                    id = entity.id,
                    itemName = entity.itemName,
                    laundryshop = entity.laundryshop,
                    category = entity.category,
                    price = entity.price
                )
            }

        val dto = LaundryDetailResDTO(
            shopName = laundryShop.shopName,
            phone = laundryShop.phone,
            address = laundryShop.address,
            nonOperatingDays = laundryShop.nonOperatingDays,
            businessNumber = laundryShop.businessNumber,
            userName = laundryShop.userName,

            handledItems = handledItems
        )

        return dto
    }

    //카테고리로 세탁소 정보 찾기
    fun findLaundryShopsByCategory(category: LaundryCategory): List<LaundryShop> {
        // HandledItems에서 카테고리에 맞는 세탁소 ID 리스트 가져오기
        val shopIds: List<Int> = handledItemsRepository.findLaundryShopIdsByCategory(category)

        // 세탁소 정보 가져오기
        return laundryShopRepository.findByIdIn(shopIds)
    }

    //세탁소 저장하기
    fun registerLaundryShop(dto: ShopAddReqDTO, id: Int): Int {
//        val user: User = userRepository.findById(id).orElse(null)
//        val shop = laundryShopRepository.findByUserId(id)
//
//        shop.user = user
//        shop.shopName = dto.shopName
//        shop.businessNumber = dto.businessNumber
//        shop.userName = dto.userName
//        shop.address = dto.address
//        shop.phone = dto.phone
//        shop.nonOperatingDays = dto.nonOperatingDays
//        shop.latitude = dto.latitude
//        shop.longitude = dto.longitude
//        shop.createdAt = Timestamp(System.currentTimeMillis())
//
//        val savedShop = laundryShopRepository.save(shop)
//
//        return savedShop.id
        return 1
    }


    //가격표 정보 등록 및 수정
    fun upsertHandledItems(items: List<HandledItemsResDTO>): List<HandledItems> {
        val laundryId = items.first().laundryId
        val laundryShop = laundryShopRepository.findById(laundryId)

        println("laundry id : $laundryId")

        // laundry_id로 저장된 가격표 가져오기
        val existingItems = handledItemsRepository.findByLaundryshopId(laundryId).toMutableList()

        // 삭제할 항목 식별 및 제거
        val toBeDeletedItems = existingItems.filterNot { existingItem ->
            items.any { it.itemName == existingItem.itemName && it.category == existingItem.category }
        }
        handledItemsRepository.deleteAll(toBeDeletedItems)

        // 기존 항목 업데이트 및 새 항목 추가
        val updatedItems = items.map { dto ->
            val existingItem = existingItems.find {
                it.itemName == dto.itemName && it.category == dto.category
            }
            existingItem?.apply {
                this.price = dto.price
            } ?: HandledItems(
                laundryshop = laundryShop,
                itemName = dto.itemName,
                category = dto.category,
                price = dto.price,
                id = dto.id
            )
        }

        // 저장 및 반환
        return handledItemsRepository.saveAll(updatedItems)
    }

}