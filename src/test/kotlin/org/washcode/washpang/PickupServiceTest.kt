package org.washcode.washpang

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.repository.HandledItemsRepository
import org.washcode.washpang.domain.laundryshop.entity.LaundryShop
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.domain.pickup.dto.PickupDto
import org.washcode.washpang.domain.pickup.entity.Pickup
import org.washcode.washpang.domain.pickup.entity.PickupItem
import org.washcode.washpang.domain.pickup.repository.PickupItemRepository
import org.washcode.washpang.domain.pickup.repository.PickupRepository
import org.washcode.washpang.domain.pickup.service.PickupService
import org.washcode.washpang.domain.user.entity.User
import org.washcode.washpang.domain.user.repository.UserRepository
import org.washcode.washpang.global.comm.enums.LaundryCategory
import org.washcode.washpang.global.comm.enums.PickupStatus
import org.washcode.washpang.global.comm.enums.UserRole
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class PickupServiceTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var laundryShopRepository: LaundryShopRepository

    @Autowired
    lateinit var handledItemsRepository: HandledItemsRepository

    @Autowired
    lateinit var pickupRepository: PickupRepository

    @Autowired
    lateinit var pickupItemRepository: PickupItemRepository

    @Autowired
    lateinit var pickupService: PickupService

    private var testPickupId: Int = 0
    private var testUserId: Int = 0

    @BeforeEach
    fun setUp() {
        // 1. 테스트 User 생성 및 저장
        val user = User(
            id = 0,
            kakaoId = null,
            name = "TestUser",
            password = "1234",
            baseAddress = "TestAddress",
            detailedAddress = "TestAddress",
            phone = "010-0000-0000",
            email = "test@kakao.com",
            role = UserRole.USER,
        )
        val savedUser = userRepository.save(user)
        testUserId = savedUser.id

        // 2. 테스트 LaundryShop 생성 및 저장
        val laundryShop = LaundryShop(
            id = 0,
            user = user,
            shopName = "TestLaundry",
            businessNumber = "010-0000-0000",
            userName = "TestUser",
            phone = "010-0000-0000",
            address = "TestAddress",
            nonOperatingDays = "일요일",
            latitude = 1.2,
            longitude = 1.4
        )
        val savedLaundryShop = laundryShopRepository.save(laundryShop)

        // 3. 테스트 Pickup 엔티티 생성 및 저장
        val pickup = Pickup(
            id = 0,
            user = savedUser,
            laundryshop = savedLaundryShop,
            status = PickupStatus.REQUESTED,
            content = "Pickup Test Content"
        )
        val savedPickup = pickupRepository.save(pickup)
        testPickupId = savedPickup.id

        // 4. 테스트 HandledItems 생성 및 저장
        val handledItem = HandledItems(
            id = 0,
            laundryshop = laundryShop,
            itemName = "TestItemName",
            category = LaundryCategory.STORAGE_SERVICE,
            price = 3000
        )
        val savedHandledItem = handledItemsRepository.save(handledItem)

        // 5. PickupItem 연결
        val pickupItem = PickupItem(
            id = 0,
            pickup = savedPickup,
            handledItems = savedHandledItem,
            quantity = 2,
            totalPrice = 6000
        )
        pickupItemRepository.save(pickupItem)
    }

    @Test
    @DisplayName("1. getPickupDetail() - 픽업 상세 정보 조회 테스트")
    @ExtendWith(CustomLogger::class)
    fun testGetPickupDetail() {
        // when
        val result: PickupDto.Res = pickupService.getPickupDetail(testPickupId.toLong())

        // then
        assertThat(result).isNotNull
        assertThat(result.pickupId).isEqualTo(testPickupId)
        assertThat(result.status).isEqualTo(PickupStatus.REQUESTED)
        assertThat(result.orderItems).hasSize(1)
        assertThat(result.orderItems[0].itemName).isEqualTo("TestItemName")
        assertThat(result.orderItems[0].totalPrice).isEqualTo(6000)
    }

    @Test
    @DisplayName("2. updatePickupStatus() - 픽업 상태 업데이트 테스트")
    @ExtendWith(CustomLogger::class)
    fun testUpdatePickupStatus() {
        // given
        val newStatus = PickupStatus.IN_PROGRESS

        // when
        pickupService.updatePickupStatus(testPickupId.toLong(), newStatus)
        val updatedPickup = pickupRepository.findById(testPickupId.toLong()).get()

        // then
        assertThat(updatedPickup.status).isEqualTo(PickupStatus.IN_PROGRESS)
    }

    @Test
    @DisplayName("3. getPickupList() - 특정 유저의 픽업 리스트 조회 테스트")
    @ExtendWith(CustomLogger::class)
    fun testGetPickupList() {
        // when
        val pickupList = pickupService.getPickupList(testUserId.toLong())

        // then
        assertThat(pickupList).isNotEmpty
        assertThat(pickupList[0].pickupId).isEqualTo(testPickupId)
        assertThat(pickupList[0].shopName).isEqualTo("TestLaundry")
        assertThat(pickupList[0].paymentAmount).isEqualTo(0)  // Payment가 없으므로 기본값 0
    }

    @Test
    @DisplayName("4. getPickedupListAndUpdateStatus() - 특정 상태 리스트 조회 & 상태 업데이트 테스트")
    @ExtendWith(CustomLogger::class)
    fun testGetPickedupListAndUpdateStatus() {
        // given
        // 현재 상태는 REQUESTED이므로 대상에 포함되지 않음
        // 우선 PAYMENT_COMPLETED로 상태를 변경해 대상에 포함시키자
        pickupService.updatePickupStatus(testPickupId.toLong(), PickupStatus.PAYMENT_COMPLETED)

        // when
        val pickedUpList = pickupService.getPickedupListAndUpdateStatus(testUserId.toLong())

        // then
        // PAYMENT_COMPLETED -> IN_PROGRESS 로 바뀌어 반환됨
        assertThat(pickedUpList).isNotEmpty
        assertThat(pickedUpList[0].pickupId).isEqualTo(testPickupId)
        assertThat(pickedUpList[0].status).isEqualTo(PickupStatus.IN_PROGRESS)
    }

    @Test
    @DisplayName("5. getPickupDeliveryList() - 배달 중(OUT_FOR_DELIVERY) 상태 픽업 조회 테스트")
    @ExtendWith(CustomLogger::class)
    fun testGetPickupDeliveryList() {
        // given
        // 현재 상태를 OUT_FOR_DELIVERY로 변경
        pickupService.updatePickupStatus(testPickupId.toLong(), PickupStatus.OUT_FOR_DELIVERY)

        // when
        val deliveryList = pickupService.getPickupDeliveryList(testUserId.toLong())

        // then
        assertThat(deliveryList).isNotEmpty
        assertThat(deliveryList[0].pickupId).isEqualTo(testPickupId)
        assertThat(deliveryList[0].shopName).isEqualTo("TestLaundry")
    }

    @Test
    @DisplayName("6. getPickupSalesSummery() - 특정 월의 정산(DELIVERED, CANCELLED 등) 리스트 조회 테스트")
    @ExtendWith(CustomLogger::class)
    fun testGetPickupSalesSummary() {
        // given
        // 상태를 DELIVERED로 변경 (정산 대상에 포함)
        pickupService.updatePickupStatus(testPickupId.toLong(), PickupStatus.DELIVERED)

        val year = LocalDateTime.now().year
        val month = LocalDateTime.now().monthValue

        // when
        val salesList = pickupService.getPickupSalesSummery(testUserId.toLong(), year, month)

        // then
        assertThat(salesList).isNotEmpty
        assertThat(salesList[0].pickupId).isEqualTo(testPickupId)
        assertThat(salesList[0].status).isEqualTo(PickupStatus.DELIVERED)
    }
}
