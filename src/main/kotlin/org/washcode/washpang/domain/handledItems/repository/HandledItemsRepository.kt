package org.washcode.washpang.domain.handledItems.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.washcode.washpang.domain.handledItems.dto.ItemInfoResDto
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.global.comm.enums.LaundryCategory
import java.util.*

interface HandledItemsRepository : JpaRepository<HandledItems, Long> {
    override fun findAll(): List<HandledItems> //모든 데이터 가져오기

    //List<HandledItems> findByLaundryshopId(Long laundryShopId);
    //세탁소 아이디를 받아서 handledItem 내용 조회(id, laundryshop_id,price, item_name, category)
    @Query("SELECT h FROM HandledItems h WHERE h.laundryshop.id = :laundryshopId")
    fun findByLaundryshopId(@Param("laundryshopId") laundryshopId: Long): List<HandledItems>

    //카테고리별로 세탁소 id 조회
    @Query("SELECT h.laundryshop.id FROM HandledItems h WHERE h.category = :category")
    fun findLaundryShopIdsByCategory(category: LaundryCategory): List<Int>

    @Query("SELECT h.id, h.itemName, h.category,h.price FROM HandledItems h WHERE h.laundryshop.id = :laundryId")
    fun findHandledItemsByLaundryId(@Param("laundryId") laundryId: Int): List<ItemInfoResDto>

    fun findById(@Param("itemId") itemId: Int): Optional<HandledItems>
}