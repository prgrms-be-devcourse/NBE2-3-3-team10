package org.washcode.washpang.domain.laundryshop.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import java.util.*

@Repository
interface LaundryShopRepository : JpaRepository<LaundryShop, Long> {
    @Query("SELECT L.shopName FROM LaundryShop L WHERE L.id = :id")
    fun findNameById(id: Int): String?

    @Query("SELECT L FROM LaundryShop L WHERE L.shopName like %:shopName% OR L.address like %:shopName%")
    fun findByShopNameContaining(@Param("shop_name") shopName: String): List<LaundryShop>

    fun findByIdIn(ids: List<Int>): List<LaundryShop>

    fun findByUserId(userId: Int): LaundryShop

    fun findById(id: Int): LaundryShop
}
