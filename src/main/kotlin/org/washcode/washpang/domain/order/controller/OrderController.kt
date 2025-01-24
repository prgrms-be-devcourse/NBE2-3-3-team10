package org.washcode.washpang.domain.order.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.order.dto.KakaoPayReqDTO
import org.washcode.washpang.domain.order.dto.OrderDto
import org.washcode.washpang.domain.order.service.OrderService

@RestController
@RequestMapping("/api/orders")
class OrderController {

    @Autowired
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var kakaoPayClient: KakaoPayClient

    @GetMapping("/info/{laundryId}")
    fun getInfo(
//        @AuthenticationPrincipal id: Int,
        @PathVariable("laundryId") laundryId: Int): ResponseEntity<*> {
        var id:Int =1
        return orderService.getInfo(id, laundryId)
    }

    @PostMapping
    fun createOrder(
//        @AuthenticationPrincipal id: Int,
        @RequestBody orderReqDTO: OrderDto.OrderReq
    ): ResponseEntity<*> {
        var id:Int =1
        return orderService.createOrder(id, orderReqDTO)
    }

    @GetMapping
    fun getOrders(
//        @AuthenticationPrincipal id: Int
    ): ResponseEntity<*> {
        var id:Int =1
        return orderService.getOrders(id)
    }

    @GetMapping("/{pickupId}")
    fun getOrderDetail(
//        @AuthenticationPrincipal id: Int,
        @PathVariable("pickupId") pickupId: Int
    ): ResponseEntity<*> {
        var id:Int =1
        return orderService.getOrdersDetail(id, pickupId)
    }

    @PostMapping("/cancel")
    fun cancelOrder(
//        @AuthenticationPrincipal id: Int,
        @RequestBody pickupId: Map<String, Int>
    ): ResponseEntity<*> {
        var id:Int =1
        return orderService.cancelOrder(id, pickupId["pickupId"] ?: 0)
    }

    @PostMapping("/kakaopay/ready")
    fun kakaoPayReady(
//        @AuthenticationPrincipal id: Int,
        @RequestBody kakaoPayReqDTO: OrderDto.KakaoPayReq
    ): ResponseEntity<*> {
        var id:Int =1
        return kakaoPayClient.payReady(id, kakaoPayReqDTO)
    }

    @PostMapping("/kakaopay/approve")
    fun kakaoPayApprove(
//        @AuthenticationPrincipal id: Int,
        @RequestBody request: Map<String, String>
    ): ResponseEntity<*> {
        var id:Int =1
        return kakaoPayClient.payCompleted(id, request["token"])
    }
}