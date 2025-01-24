package org.washcode.washpang.domain.order.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.repository.HandledItemsRepository
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.domain.order.dto.OrderDto
import org.washcode.washpang.domain.order.repository.db.PaymentRepository
import org.washcode.washpang.domain.order.entity.db.Payment
import org.washcode.washpang.domain.order.repository.redis.KakaoPayPgTokenRepository
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.domain.pickup.entity.PickupItem
import org.washcode.washpang.domain.pickup.repository.PickupItemRepository
import org.washcode.washpang.domain.pickup.repository.PickupRepository
import org.washcode.washpang.domain.user.entity.User

import org.washcode.washpang.global.comm.enums.PickupStatus
import java.sql.Timestamp
import java.text.SimpleDateFormat

@Service
class OrderService(
    private val pickupRepository: PickupRepository,
    private val userRepository: UserRepository,
    private val laundryShopRepository: LaundryShopRepository,
    private val handledItemsRepository: HandledItemsRepository,
    private val pickupItemRepository: PickupItemRepository,
    private val paymentRepository: PaymentRepository,
    private val kakaoPayPgTokenRepository: KakaoPayPgTokenRepository
) {

    @Transactional
    fun cancelOrder(userId: Int, pickupId: Int): ResponseEntity<*> {
        val updatedRows = pickupRepository.cancleOrder(pickupId, userId)
        return if (updatedRows == 0) {
            ResponseEntity.status(500).body("No matching pickup found for pickupId: $pickupId and userId: $userId")
        } else {
            ResponseEntity.ok().body("취소완료")
        }
    }

    fun getInfo(id: Int, laundryId: Int): ResponseEntity<*> {
        val orderInfoResDTO = OrderDto.InfoRes(
            name = userRepository.findNameById(id).get(),
            address = userRepository.findAddressById(id).get(),
            shopName = laundryShopRepository.findNameById(laundryId).get(),
            category = handledItemsRepository.findHandledItemsByLaundryId(laundryId)
        )

        return ResponseEntity.ok().body(orderInfoResDTO)
    }

    fun createOrder(id: Int, orderReqDTO: OrderDto.OrderReq): ResponseEntity<*> {
        return try {
            val user = fetchUserById(id)
            val laundryshop = fetchLaundryShopById(orderReqDTO.laundryshopId)
            val pickup = createAndSavePickup(user, laundryshop, orderReqDTO.content)
            val handledItem = fetchHandledItemById(orderReqDTO.itemId)
            createAndSavePickupItem(pickup, handledItem, orderReqDTO.quantity)
            createAndSavePayment(pickup, handledItem, orderReqDTO)

            ResponseEntity.ok().body(pickupRepository.findIdByMax())
        } catch (e: Exception) {
            println("[Error] ${e.message}")
            ResponseEntity.status(500).body("DB 에러")
        }
    }

    private fun fetchUserById(id: Int) = userRepository.findById(id)
        .orElseThrow { IllegalArgumentException("User not found with id: $id") }

    private fun fetchLaundryShopById(id: Int) = laundryShopRepository.findById(id)
        ?: throw IllegalArgumentException("LaundryShop not found with id: $id")

    private fun fetchHandledItemById(id: Int) = handledItemsRepository.findById(id)
        ?: throw IllegalArgumentException("HandledItem not found with id: $id")

    private fun createAndSavePickup(user: User, laundryshop: LaundryShop, content: String): Pickup {
        val pickup = Pickup(
            id = 0,
            user = user,
            laundryshop = laundryshop,
            status = PickupStatus.REQUESTED,
            content = content
        ).apply {
            createdAt = Timestamp(System.currentTimeMillis())
        }
        return pickupRepository.save(pickup)
    }

    private fun createAndSavePickupItem(pickup: Pickup, handledItem: HandledItems, quantity: Int) {
        val pickupItem = PickupItem(
            id = 0,
            pickup = pickup,
            handledItems = handledItem,
            quantity = quantity,
            totalPrice = handledItem.price * quantity
        )
        pickupItemRepository.save(pickupItem)
    }

    private fun createAndSavePayment(pickup: Pickup, handledItem: HandledItems, orderReqDTO: OrderDto.OrderReq) {
        val payment = Payment(
            id = 0,
            pickup = pickup,
            paymentDatetime = Timestamp(System.currentTimeMillis()),
            amount = handledItem.price * orderReqDTO.quantity,
            method = orderReqDTO.paymentMethod,
            aid = "",
            tid = "",
            paymentMethodType = "",
            createdAt = Timestamp(System.currentTimeMillis()).toString(),
            approvedAt = "",
            payload = ""
        )
        paymentRepository.save(payment)
    }



    // 유저 ID 로 주문내역 조회
    fun getOrders(id: Int): ResponseEntity<*> {
        val result: List<Array<Any>> = pickupRepository.findOrderListByUserId(id)

        val orderlistResDTOS = result.map { row ->
            OrderDto.listRes(
                row[1] as Int,
                row[0] as String,
                (row[2] as PickupStatus).desc,
                SimpleDateFormat("yyyy년 MM월 dd일").format(row[3] as Timestamp)
            )
        }

        return ResponseEntity.ok().body(orderlistResDTOS)
    }

    // 유저 ID 및 주문 ID로 주문 상세 내역 조회
    fun getOrdersDetail(id: Int, pickupId: Int): ResponseEntity<*> {
        val result = pickupRepository.findOrderDetails(id, pickupId)
        val orderResDTO = OrderDto.OrderRes(
            name = "",
            address = "",
            phone = "",
            shopName = "",
            content = "",
            createdAt = "",
            updateAt = "",
            paymentId = 0,
            method = "",
            amount = 0,
            price = 0,
            status = ""
        ).apply {
            orderItems = mutableListOf()
        }

        for (obj in result) {
            orderResDTO.apply {
                address = obj[0] as String
                phone = obj[1] as String
                shopName = obj[2] as String
                content = obj[5] as String
                name = obj[15] as String
                val status = (obj[4] as PickupStatus).desc
                createdAt = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(obj[6] as Timestamp)
                updateAt = obj[7]?.let { SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(it as Timestamp) }?:""
                method = obj[14] as String
                amount = obj[13] as Int
                paymentId = obj[17] as Int
            }

            val orderItem = OrderDto.OrderRes.OrderItem(
                obj[11] as String,
                obj[9] as Int,
                obj[10] as Int
            )
            orderResDTO.orderItems.add(orderItem)
        }

        return ResponseEntity.ok().body(orderResDTO)
    }

    // 결제 대기 -> 결제 완료 바꾸는 메소드
    @Transactional
    fun updatePaymentStatusComplete(pgToken: String) {
        val kakaoPayPgToken = kakaoPayPgTokenRepository.findById("pgToken:$pgToken")
            .orElseThrow { IllegalArgumentException("No matching pgToken found for [pgToken:$pgToken]") }
        kakaoPayPgTokenRepository.deleteById("pgToken:$pgToken")

        val paymentId = kakaoPayPgToken.partnerOrderId
        val pickup = paymentRepository.findPickUpById(paymentId)
            ?: throw IllegalArgumentException("No matching payment found for paymentId: $paymentId")

//            .orElseThrow { IllegalArgumentException("No matching payment found for paymentId: $paymentId") }
        pickup.status = PickupStatus.PAYMENT_COMPLETED
    }
}
