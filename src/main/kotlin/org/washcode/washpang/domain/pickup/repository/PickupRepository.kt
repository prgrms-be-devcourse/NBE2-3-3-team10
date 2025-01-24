package org.washcode.washpang.domain.pickup.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.global.comm.enums.PickupStatus
import java.sql.Timestamp
import java.util.*

@Repository
interface PickupRepository : JpaRepository<Pickup, Long> {

    @Query(
        "SELECT DISTINCT p FROM Pickup p " +
                "JOIN FETCH p.user u " +
                "JOIN FETCH p.laundryshop l " +
                "WHERE p.id = :pickupId"
    )
    fun findPickupWithFetchJoin(
        @Param("pickupId") pickupId: Long
    ): Optional<Pickup>

    @Query(
        "SELECT DISTINCT p FROM Pickup p " +
                "JOIN FETCH p.user u " +
                "JOIN FETCH p.laundryshop l " +
                "WHERE p.laundryshop.user.id = :userId " +
                "AND p.status = :status"
    )
    fun findAllByUserIdWithFetchJoinAndStatus(
        @Param("userId") userId: Long,
        @Param("status") status: PickupStatus
    ): List<Pickup>

    @Query(
        "SELECT DISTINCT p FROM Pickup p " +
                "JOIN FETCH p.user u " +
                "WHERE p.laundryshop.user.id = :userId " +
                "AND p.status IN :statuses " +
                "ORDER BY p.updateAt DESC"
    )
    fun findAllByUserIdAndStatuses(
        @Param("userId") userId: Long,
        @Param("statuses") statuses: List<PickupStatus>
    ): List<Pickup>

    //이용내역 조회
    @Query(
        "SELECT ls.shopName, p.id, p.status, p.createdAt " +
                "FROM LaundryShop ls " +
                "JOIN Pickup p ON ls.id = p.laundryshop.id " +
                "WHERE p.user.id = :userId " +
                "ORDER BY p.createdAt DESC"
    )
    fun findOrderListByUserId(
        @Param("userId") userId: Int
    ): List<Array<Any>>


    // 필터링된 데이터 가져오기(개월 수)
    @Query(
        "SELECT ls.shopName, p.id, p.status, p.createdAt " +
                "FROM LaundryShop ls " +
                "JOIN Pickup p ON ls.id = p.laundryshop.id " +
                "WHERE p.user.id = :userId AND p.createdAt >= :fromDate"
    )
    fun findByUserIdAndDate(
        @Param("userId") userId: Int,
        @Param("fromDate") fromDate: Timestamp
    ): List<Array<Any>>


    //이용내역 조회(상세보기)
    @Query(
        "SELECT " +
                "u.baseAddress AS baseAddress, " +
                "u.phone AS phone, " +
                "ls.shopName AS shopName, " +
                "p.id AS pickupId, " +
                "p.status AS status, " +
                "p.content AS content, " +
                "p.createdAt AS pickupCreatedAt, " +
                "p.updateAt AS pickupUpdateAt, " +
                "pi.id AS pickupItemId, " +
                "pi.quantity AS quantity, " +
                "pi.totalPrice AS totalPrice, " +
                "hi.itemName AS itemName, " +
                "hi.category AS category, " +
                "pay.amount AS amount, " +
                "pay.method AS method, " +
                "u.name AS name, " +
                "pay.payment_datetime AS paymentDateTime, " +
                "pay.id AS paymentId " +
                "FROM User u " +
                "JOIN Pickup p ON u.id = p.user.id " +
                "JOIN LaundryShop ls ON ls.id = p.laundryshop.id " +
                "LEFT JOIN PickupItem pi ON p.id = pi.pickup.id " +
                "LEFT JOIN HandledItems hi ON pi.handledItems.id = hi.id " +
                "LEFT JOIN Payment pay ON p.id = pay.pickup.id " +
                "WHERE u.id = :userId AND pi.pickup.id = :pickupId"
    )
    fun findOrderDetails(
        @Param("userId") userId: Int,
        @Param("pickupId") pickupId: Int
    ): List<Array<Any>>

    @Transactional
    @Modifying
    @Query(
        "UPDATE Pickup p " +
                "SET p.status = 'CANCELLED' " +
                "WHERE p.id = :pickupId AND p.user.id = :userId"
    )
    fun cancleOrder(pickupId: Int, userId: Int): Int


    @Query(
        "SELECT DISTINCT p FROM Pickup p " +
                "JOIN FETCH p.user u " +
                "JOIN FETCH p.laundryshop l " +
                "WHERE p.laundryshop.user.id = :userId " +
                "AND p.status IN :statuses " +
                "AND YEAR(p.createdAt) = :year " +
                "AND MONTH(p.createdAt) = :month " +
                "ORDER BY p.createdAt DESC"
    )
    fun findSalesSummeryByUserIdAndDate(
        @Param("userId") userId: Long,
        @Param("statuses") statuses: List<PickupStatus>,
        @Param("year") year: Int,
        @Param("month") month: Int
    ): List<Pickup>

    @Query("SELECT MAX(p.id) AS pickup_id FROM Pickup p")
    fun findIdByMax(): Int
}