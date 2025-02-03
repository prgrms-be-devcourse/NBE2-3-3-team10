package org.washcode.washpang

import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.util.ReflectionTestUtils
import org.washcode.washpang.domain.order.dto.OrderDto
import org.washcode.washpang.domain.order.service.OrderService
import org.washcode.washpang.domain.pickup.dto.PickupDto
import org.washcode.washpang.domain.pickup.service.PickupService
import org.washcode.washpang.global.comm.enums.PickupStatus
import org.washcode.washpang.global.domain.kakao.client.KakaoClient
import org.washcode.washpang.global.domain.kakao.dto.KakaoDto
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class WashCodeApplicationTests {

    @Autowired
    lateinit var pickupService: PickupService

    @Autowired
    lateinit var orderService: OrderService

    @MockitoBean
    lateinit var kakaoClient: KakaoClient

    @BeforeEach
    fun setUp() {

        val testUser = KakaoDto.Data(1L,"dummyEmail@kakao.com", "dummyNickname", "1234")
        val result = ReflectionTestUtils.invokeMethod<KakaoDto.Data>(
            kakaoClient,
            "getKakaoUserData",
            "Bearer 12556"
        )

        org.mockito.Mockito.`when`(result).thenReturn(testUser)

        val testOrderItem = PickupDto.OrderItem("testItemNM", 2, 3000)
        val testOrderItems = mutableListOf<PickupDto.OrderItem>()
        testOrderItems.add(testOrderItem)

        val testOrderReq = OrderDto.OrderReq(
            laundryshopId = 1,
            content = "testContent",
            itemId = 1,
            quantity = 1,
            paymentMethod = "kakaoPay")

        orderService.createOrder(1, testOrderReq)

        val testPickUp = PickupDto.DetailRes(
            pickupId = 1,
            shopName = "testName",
            createdAt = Timestamp.valueOf(LocalDateTime.now()),
            baseAddress = "testAddress",
            phone = "testPhoneNumber",
            content = "테스트용",
            orderItems = testOrderItems,
            paymentAmount = 6000,
            paymentMethod = "kakaoPay"
        )
    }

    @DisplayName("픽업 세부 정보를 조회해서 가져오는 테스트")
    @ExtendWith(CustomLogger::class)
    @Test
    fun getPickupDetailTest() {
        val result = pickupService.getPickupDetail(1L)

        val orderItems = mutableListOf<PickupDto.OrderItem>().apply {
            add(PickupDto.OrderItem("패딩입니당", 2, 3000))
        }

        val mockPickupRes = PickupDto.Res(
            pickupId = 1,
            status = PickupStatus.REQUESTED,
            createdAt = Timestamp.from(Instant.now()),
            baseAddress = "테스트 주소",
            content = "테스트 설명",
            orderItems = orderItems
        )

        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.orderItems).hasSize(1)
        Assertions.assertThat(result.orderItems[0].itemName).isEqualTo("패딩입니당")
    }
}
