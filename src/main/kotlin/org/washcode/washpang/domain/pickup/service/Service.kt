package org.washcode.washpang.domain.pickup.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.washcode.washpang.domain.pickup.dto.PickupDTO
import org.washcode.washpang.domain.pickup.dto.PickupDTO.Res.OrderItemDTO
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.domain.pickup.entity.PickupItem
import org.washcode.washpang.domain.pickup.repository.PickupItemRepository
import org.washcode.washpang.domain.pickup.repository.PickupRepository

@Service
class Service(
    private val pickupRepository: PickupRepository,
    private val paymentRepository: PaymentRepository,
    private val pickupItemRepository: PickupItemRepository,
) {
    @Transactional
    fun getPickupDetail(pickupId: Long): PickupDTO.Res {
        val pickup: Pickup = pickupRepository.findPickupWithFetchJoin(pickupId)
            .orElseThrow {
                RuntimeException("해당 ID로 pickup 을 찾을 수 없습니다: $pickupId")
            }

        val pickupItems: List<PickupItem> = pickupItemRepository.findByPickupId(pickupId)

        val orderItems: List<OrderItemDTO> = pickupItems.map { item ->
            OrderItemDTO(
                itemName = item.handledItems.itemName,
                quantity = item.quantity,
                totalPrice = item.totalPrice
            )
        }

        return PickupDTO.Res(
            pickupId = pickup.id,
            status = pickup.status,
            createdAt = pickup.createdAt,
            address = pickup.user.address,
            content = pickup.content,
            orderItems = orderItems
        )
    }

    @Transactional
    fun getPickupList(userId: Long): List<PickupDTO.DetailRes> {
        val pickups: List<Pickup> =
            pickupRepository.findAllByUserIdWithFetchJoinAndStatus(userId, PickupStatus.REQUESTED)

        return pickups.map { pickup ->
            val payment: Payment = paymentRepository.findByPickupId(pickup.id.toLong())
            val pickupItems: List<PickupItem> = pickupItemRepository.findByPickupId(pickup.id.toLong())

            val orderItems: List<PickupDTO.DetailRes.OrderItemDTO> = pickupItems.map { item ->
                PickupDTO.DetailRes.OrderItemDTO(
                    itemName = item.handledItems.itemName,
                    quantity = item.quantity,
                    totalPrice = item.totalPrice
                )
            }

            PickupDTO.DetailRes(
                pickupId = pickup.id,
                shopName = pickup.laundryshop.shopName,
                createdAt = pickup.createdAt,
                address = pickup.user.address,
                phone = pickup.user.phone,
                content = pickup.content,
                orderItems = orderItems,
                paymentAmount = payment.amount,
                paymentMethod = payment.method
            )
        }
    }

    @Transactional
    fun updatePickupStatus(pickupId: Long, newStatus: PickupStatus){
        val pickup = pickupRepository.findById(pickupId)
            .orElseThrow { java.lang.RuntimeException("해당 ID로 pickup을 찾을 수 없습니다: $pickupId") }

        pickup.status = newStatus
        pickupRepository.save(pickup)
    }

}