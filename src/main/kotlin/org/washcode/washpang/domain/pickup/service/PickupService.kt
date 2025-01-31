package org.washcode.washpang.domain.pickup.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.washcode.washpang.domain.order.entity.db.Payment
import org.washcode.washpang.domain.order.repository.db.PaymentRepository
import org.washcode.washpang.domain.pickup.dto.PickupDto
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.domain.pickup.entity.PickupItem
import org.washcode.washpang.domain.pickup.repository.PickupItemRepository
import org.washcode.washpang.domain.pickup.repository.PickupRepository
import org.washcode.washpang.global.comm.enums.PickupStatus
import java.util.*

@Service
class PickupService(
    private val pickupRepository: PickupRepository,
//    private val paymentRepository: PaymentRepository,
    private val pickupItemRepository: PickupItemRepository,
    private val paymentRepository: PaymentRepository,
) {
    @Transactional
    fun getPickupDetail(pickupId: Long): PickupDto.Res {
        val pickup: Pickup = pickupRepository.findPickupWithFetchJoin(pickupId)
            .orElseThrow {
                RuntimeException("해당 ID로 pickup 을 찾을 수 없습니다: $pickupId")
            }

        val pickupItems: List<PickupItem> = pickupItemRepository.findByPickupId(pickupId)

        val orderItems: List<PickupDto.OrderItem> = pickupItems.map { item ->
            PickupDto.OrderItem(
                itemName = item.handledItems.itemName,
                quantity = item.quantity,
                totalPrice = item.totalPrice
            )
        }

        return PickupDto.Res(
            pickupId = pickup.id,
            status = pickup.status,
            createdAt = pickup.createdAt,
            address = pickup.user.detailedAddress,
            content = pickup.content,
            orderItems = orderItems
        )
    }

    @Transactional
    fun getPickupList(userId: Long): List<PickupDto.DetailRes> {
        val pickups: List<Pickup> =
            pickupRepository.findAllByUserIdWithFetchJoinAndStatus(userId, PickupStatus.REQUESTED)

        return pickups.map { pickup ->
            val payment: Payment? = paymentRepository.findByPickupId(pickup.id.toLong())
            val pickupItems: List<PickupItem> = pickupItemRepository.findByPickupId(pickup.id.toLong())

            val orderItems: List<PickupDto.OrderItem> = pickupItems.map { item ->
                PickupDto.OrderItem(
                    itemName = item.handledItems.itemName,
                    quantity = item.quantity,
                    totalPrice = item.totalPrice
                )
            }

            PickupDto.DetailRes(
                pickupId = pickup.id,
                shopName = pickup.laundryshop.shopName,
                createdAt = pickup.createdAt,
                address = pickup.user.baseAddress,
                phone = pickup.user.phone,
                content = pickup.content,
                orderItems = orderItems,
                paymentAmount = payment?.amount ?: 0,
                paymentMethod = payment?.method ?: "NONE"
            )
        }
    }

    @Transactional
    fun updatePickupStatus(pickupId: Long, newStatus: PickupStatus) {
        val pickup = pickupRepository.findById(pickupId)
            .orElseThrow { java.lang.RuntimeException("해당 ID로 pickup을 찾을 수 없습니다: $pickupId") }

        pickup.status = newStatus
        pickupRepository.save(pickup)
    }

    @Transactional
    fun getPickedupListAndUpdateStatus(userId: Long): List<PickupDto.Res> {
        val statuses = Arrays.asList(
            PickupStatus.PAYMENT_PENDING,
            PickupStatus.PAYMENT_COMPLETED,
            PickupStatus.IN_PROGRESS,
            PickupStatus.CANCELLED_WITH_DELIVERY_FEE,
            PickupStatus.CANCELLED
        )

        val pickups = pickupRepository.findAllByUserIdAndStatuses(userId, statuses)

        return pickups.map { pickup: Pickup ->
            val pickupItems = pickupItemRepository.findByPickupId(pickup.id.toLong())
            val orderItems: List<PickupDto.OrderItem> = pickupItems
                .map { item: PickupItem ->
                    PickupDto.OrderItem(
                        itemName = item.handledItems.itemName,
                        quantity = item.quantity,
                        totalPrice = item.totalPrice
                    )
                }

            if (pickup.status == PickupStatus.PAYMENT_COMPLETED) {
                pickup.status = PickupStatus.IN_PROGRESS
                pickupRepository.save(pickup)
            }
            PickupDto.Res(
                pickupId = pickup.id,
                status = pickup.status,
                createdAt = pickup.createdAt,
                address = pickup.user.baseAddress,
                content = pickup.content,
                orderItems
            )
        }
    }

    @Transactional
    fun getPickupDeliveryList(userId: Long): List<PickupDto.DeliveryRes> {
        val pickups = pickupRepository.findAllByUserIdWithFetchJoinAndStatus(userId, PickupStatus.OUT_FOR_DELIVERY)

        return pickups.map { pickup: Pickup ->
            val pickupItems = pickupItemRepository.findByPickupId(pickup.id.toLong())
            val orderItems: List<PickupDto.OrderItem> = pickupItems
                .map { item: PickupItem ->
                    PickupDto.OrderItem(
                        itemName = item.handledItems.itemName,
                        quantity = item.quantity,
                        totalPrice = item.totalPrice
                    )
                }
            PickupDto.DeliveryRes(
                pickupId = pickup.id,
                shopName = pickup.laundryshop.shopName,
                createdAt = pickup.createdAt,
                address = pickup.user.baseAddress,
                phone = pickup.user.phone,
                content = pickup.content,
                orderItems
            )
        }
    }

    @Transactional
    fun getPickupSalesSummery(userId: Long, year: Int, month: Int): List<PickupDto.SalesSummery> {
        val statuses = listOf(
            PickupStatus.DELIVERED,
            PickupStatus.CANCELLED_WITH_DELIVERY_FEE,
            PickupStatus.CANCELLED
        )
        val pickups = pickupRepository.findSalesSummeryByUserIdAndDate(userId, statuses, year, month)

        return pickups.map { pickup: Pickup ->
            val pickupItems = pickupItemRepository.findByPickupId(pickup.id.toLong())
            val orderItems: List<PickupDto.OrderItem> = pickupItems
                .map { item: PickupItem ->
                    PickupDto.OrderItem(
                        itemName = item.handledItems.itemName,
                        quantity = item.quantity,
                        totalPrice = item.totalPrice
                    )
                }
            println(
                "${pickup.id}/  ${pickup.status}/ ${pickup.createdAt}/ ${pickup.user.baseAddress}/ ${orderItems.size}"
            )
            PickupDto.SalesSummery(
                pickupId = pickup.id,
                status = pickup.status,
                createdAt = pickup.createdAt,
                address = pickup.user.baseAddress,
                orderItems
            )
        }
    }

}