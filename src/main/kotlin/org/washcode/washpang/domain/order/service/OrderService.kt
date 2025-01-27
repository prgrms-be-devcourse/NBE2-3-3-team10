package org.washcode.washpang.domain.order.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.washcode.washpang.domain.order.dto.OrderDto
import org.washcode.washpang.domain.order.repository.db.PaymentRepository
import org.washcode.washpang.domain.order.entity.db.Payment
import org.washcode.washpang.domain.order.repository.redis.KakaoPayPgTokenRepository
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.domain.pickup.entity.PickupItem

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
            // 사용자 및 세탁소 찾기
            val user = userRepository.findById(id).orElseThrow()
            val laundryshop = laundryShopRepository.findById(orderReqDTO.laundryshopId).orElseThrow()
            val handledItem = handledItemsRepository.findById(orderReqDTO.itemId).orElseThrow()

            // Pickup 객체 생성 (createdAt은 자동으로 설정됨)
            val pickup = Pickup(
                user = user,
                laundryshop = laundryshop,
                status = PickupStatus.REQUESTED,
                content = orderReqDTO.content
            )
            pickupRepository.save(pickup)  // 이때 createdAt이 자동으로 설정됨

            // PickupItem 객체 생성
            val pickupItem = PickupItem(
                pickup = pickup,
                handledItems = handledItem,
                quantity = orderReqDTO.quantity,
                totalPrice = handledItem.price * orderReqDTO.quantity
            )
            pickupItemRepository.save(pickupItem)

            // Payment 객체 생성
            val payment = Payment(
                pickup = pickup,
                amount = handledItem.price * orderReqDTO.quantity,
                method = orderReqDTO.paymentMethod ?: throw IllegalArgumentException("Payment method cannot be null"),
                paymentDatetime = Timestamp(System.currentTimeMillis())  // 결제 시간
            )
            paymentRepository.save(payment)

            ResponseEntity.ok().body(pickupRepository.findIdByMax())
        } catch (e: Exception) {
            println("[Error] ${e.message}")
            ResponseEntity.status(500).body("DB 에러")
        }
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
        val orderResDTO = OrderDto.OrderRes().apply {
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
                updateAt = obj[7]?.let { SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(it as Timestamp) }
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
            .orElseThrow { IllegalArgumentException("No matching payment found for paymentId: $paymentId") }

        pickup.status = PickupStatus.PAYMENT_COMPLETED
    }
}
