package org.washcode.washpang.domain.order.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.repository.HandledItemsRepository
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.laundryshop.exception.FailToFindfLaundryShopException
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.domain.order.dto.OrderDto
import org.washcode.washpang.domain.order.repository.PaymentRepository
import org.washcode.washpang.domain.order.exception.FailToFindOrderItemsException
import org.washcode.washpang.global.domain.kakaopay.redis.db.KakaoPayPgTokenRepository
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.domain.pickup.entity.PickupItem
import org.washcode.washpang.domain.pickup.repository.PickupItemRepository
import org.washcode.washpang.domain.pickup.repository.PickupRepository
import org.washcode.washpang.domain.user.entity.User
import org.washcode.washpang.domain.user.exception.NoUserDataException
import org.washcode.washpang.domain.user.repository.UserRepository

import org.washcode.washpang.global.comm.enums.PickupStatus
import org.washcode.washpang.global.domain.kakaopay.redis.entity.KakaoPayPgToken
import org.washcode.washpang.global.exception.ErrorCode
import org.washcode.washpang.global.exception.ResponseResult
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

    fun getInfo(id: Int, laundryId: Int): ResponseResult {
        try {
            val user = userRepository.findById(id)
                ?: throw NoUserDataException()
            val laundryShopName = laundryShopRepository.findNameById(laundryId)
                ?: throw FailToFindfLaundryShopException()
            val category = handledItemsRepository.findHandledItemsByLaundryId(laundryId)
                ?: throw FailToFindOrderItemsException()

            val orderInfoResDTO = OrderDto.InfoRes(
                name = user.name,
                baseAddress = user.baseAddress,
                detailedAddress = user.detailedAddress,
                shopName = laundryShopName,
                category = category
            )

            return ResponseResult(orderInfoResDTO) // 정상 200 OK!
        } catch (e: NoUserDataException) {
            return ResponseResult(ErrorCode.FAIL_TO_FIND_USER) // 1. ErrorCode Enum을 사용한 케이스
            // return ResponseResult(400, "존재하지 않는 회원입니다") 2. 직접적으로 Status Code와 Message를 지정한 케이스
        } catch (e: FailToFindfLaundryShopException) {
            return ResponseResult(ErrorCode.FAIL_TO_FIND_LAUNDRYSHOP)
        } catch (e: FailToFindOrderItemsException) {
            return ResponseResult(ErrorCode.FAIL_TO_FIND_ORDERITEMS)
        }
    }



//    fun createOrder(id: Int, orderReqDTO: OrderDto.OrderReq): ResponseEntity<*> {
//        return try {
//            val user = fetchUserById(id)
//            val laundryshop = fetchLaundryShopById(orderReqDTO.laundryshopId)
//            val pickup = user?.let { createAndSavePickup(it, laundryshop, orderReqDTO.content) }
//            val handledItem = fetchHandledItemById(orderReqDTO.itemId)
//            if (pickup != null) {
//                createAndSavePickupItem(pickup, handledItem, orderReqDTO.quantity)
//            }
//            if (pickup != null) {
//                createAndSavePayment(pickup, handledItem, orderReqDTO)
//            }
//
//            ResponseEntity.ok().body(pickupRepository.findIdByMax())
//        } catch (e: Exception) {
//            println("[Error] ${e.message}")
//            ResponseEntity.status(500).body("DB 에러")
//        }
//    }

//    private fun fetchUserById(id: Int) = userRepository.findById(id)
//        .orElseThrow { IllegalArgumentException("User not found with id: $id") }

    private fun fetchLaundryShopById(id: Int) = laundryShopRepository.findById(id)
        ?: throw IllegalArgumentException("LaundryShop not found with id: $id")

    private fun fetchHandledItemById(id: Int) = handledItemsRepository.findById(id)
        ?: throw IllegalArgumentException("HandledItem not found with id: $id")

//    private fun createAndSavePickup(user: User, laundryshop: LaundryShop, content: String): Pickup {
//        val pickup = Pickup(
//            id = 0,
//            user = user,
//            laundryshop = laundryshop,
//            status = PickupStatus.REQUESTED,
//            content = content
//        ).apply {
//            createdAt = Timestamp(System.currentTimeMillis())
//        }
//        return pickupRepository.save(pickup)
//    }
//
//    private fun createAndSavePickupItem(pickup: Pickup, handledItem: HandledItems, quantity: Int) {
//        val pickupItem = PickupItem(
//            id = 0,
//            pickup = pickup,
//            handledItems = handledItem,
//            quantity = quantity,
//            totalPrice = handledItem.price * quantity
//        )
//        pickupItemRepository.save(pickupItem)
//    }

//    private fun createAndSavePayment(pickup: Pickup, handledItem: HandledItems, orderReqDTO: OrderDto.OrderReq) {
//        val payment = Payment(
//            id = 0,
//            pickup = pickup,
//            paymentDatetime = Timestamp(System.currentTimeMillis()),
//            amount = handledItem.price * orderReqDTO.quantity,
//            method = orderReqDTO.paymentMethod,
//            aid = "",
//            tid = "",
//            paymentMethodType = "",
//            createdAt = Timestamp(System.currentTimeMillis()).toString(),
//            approvedAt = "",
//            payload = ""
//        )
//        paymentRepository.save(payment)
//    }



    // 유저 ID 로 주문내역 조회
//    fun getOrders(id: Int): ResponseEntity<*> {
//        val result: List<Array<Pickup>> = pickupRepository.findOrderListByUserId(id)
//
//        val orderlistResDTOS = result.map { row ->
//            OrderDto.listRes(
//                row[1] as Int,
//                row[0] as String,
//                (row[2] as PickupStatus).desc,
//                SimpleDateFormat("yyyy년 MM월 dd일").format(row[3] as Timestamp)
//            )
//        }
//
//        return ResponseEntity.ok().body(orderlistResDTOS)
//    }

    // 유저 ID 및 주문 ID로 주문 상세 내역 조회
    @Transactional
    fun getOrdersDetail(id: Int, pickupId: Int): ResponseEntity<*> {
        print("id: $id, pickupId: $pickupId")
        val result = pickupRepository.findOrderDetail(id, pickupId)
            ?: return ResponseEntity.badRequest().body("No order found.")

        val orderItems: MutableList<OrderDto.OrderItem> = mutableListOf()

        orderItems.add(
            OrderDto.OrderItem(
                itemName = "test",
                quantity = 1,
                totalPrice = 1234
            )
        )

        orderItems.add(
            OrderDto.OrderItem(
                itemName = "test1",
                quantity = 2,
                totalPrice = 12345
            )
        )

        // 이제 모은 정보를 한 번에 넘겨서, 불변 프로퍼티를 가진 DTO를 생성
        val orderResDTO = OrderDto.OrderRes(
            name         = result.user.name,
            address      = result.user.baseAddress,
            phone        = result.user.phone,
            shopName     = result.laundryshop.shopName,
            content      = result.content,
            status       = result.status.desc,
            createdAt    = result.createdAt.toString(),
            updateAt     = result.updateAt.toString(),
            paymentId    = result.payment.id,
            method       = result.payment.method,
            amount       = result.payment.amount,
            price        = 0,
            orderItems   = orderItems,
            paymentDatetime = Timestamp(System.currentTimeMillis())
        )

        return ResponseEntity.ok().body(orderResDTO)
    }

//    @Transactional
//    fun getOrdersDetail(id: Int, pickupId: Int): ResponseEntity<*> {
//        val result = pickupRepository.findOrderDetails(id, pickupId)
//
//        // 결과가 비어 있으면 처리
//        if (result.isEmpty()) {
//            return ResponseEntity.badRequest().body("No order found.")
//        }
//
//        // 여러 Row(픽업 하나에 여러 아이템 등)에서 공통되는 필드들을 임시 변수에 담아둠
//        var address = ""
//        var phone = ""
//        var shopName = ""
//        var content = ""
//        var name = ""
//        var status = ""
//        var createdAtStr = ""
//        var updateAtStr = ""
//        var method = ""
//        var amount = 0
//        var paymentId = 0
//
//        // 여러 아이템을 담을 리스트 (나중에 불변 List로 넣을 수 있음)
//        val orderItems = mutableListOf<OrderDto.OrderItem>()
//
//        // result가 여러 Row일 수 있으니, 각 Row마다 필요한 값을 추출
//        for (obj in result) {
//            // 공통 필드 (Pickup 자체 정보)
//            address = obj[0] as String
//            phone = obj[1] as String
//            shopName = obj[2] as String
//            status = (obj[4] as PickupStatus).desc
//            content = obj[5] as String
//            createdAtStr = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(obj[6] as Timestamp)
//            updateAtStr = (obj[7] as? Timestamp)?.let {
//                SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(it)
//            } ?: ""
//            amount = obj[13] as Int
//            method = obj[14] as String
//            name = obj[15] as String
//            paymentId = obj[17] as Int
//
//            // 아이템 정보 (Row마다 다른 아이템일 수 있음)
//            val orderItem = OrderDto.OrderItem(
//                itemName = obj[11] as String,
//                quantity = obj[9] as Int,
//                totalPrice = obj[10] as Int
//            )
//            orderItems.add(orderItem)
//        }
//
//        // 이제 모은 정보를 한 번에 넘겨서, 불변 프로퍼티를 가진 DTO를 생성
//        val orderResDTO = OrderDto.OrderRes(
//            name         = name,
//            address      = address,
//            phone        = phone,
//            shopName     = shopName,
//            content      = content,
//            status       = status,
//            createdAt    = createdAtStr,
//            updateAt     = updateAtStr,
//            paymentId    = paymentId,
//            method       = method,
//            amount       = amount,
//            price        = 0,
//            orderItems   = orderItems,
//            paymentDatetime = Timestamp(System.currentTimeMillis())
//        )
//
//        return ResponseEntity.ok().body(orderResDTO)
//    }

    // 결제 대기 -> 결제 완료 바꾸는 메소드
    @Transactional
    fun updatePaymentStatusComplete(kakaoPayPgToken: KakaoPayPgToken) {
        val paymentId = kakaoPayPgToken.partnerOrderId
        val pickup = paymentRepository.findPickUpById(paymentId)
            ?: throw IllegalArgumentException("No matching payment found for paymentId: $paymentId")

//            .orElseThrow { IllegalArgumentException("No matching payment found for paymentId: $paymentId") }
        pickup.status = PickupStatus.PAYMENT_COMPLETED
    }
}