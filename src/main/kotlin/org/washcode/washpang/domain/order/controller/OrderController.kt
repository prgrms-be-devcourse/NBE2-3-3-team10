package org.washcode.washpang.domain.order.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.order.dto.OrderDto
import org.washcode.washpang.domain.order.service.OrderService
import org.washcode.washpang.global.domain.kakao.dto.KakaoDto
import org.washcode.washpang.global.domain.kakaopay.client.KakaoPayClient
import org.washcode.washpang.global.domain.kakaopay.dto.KakaoPayDto
import org.washcode.washpang.global.exception.ResponseResult

@RestController
@RequestMapping("/api/orders")
class OrderController (
    private val orderService: OrderService,
    private val kakaoPayClient: KakaoPayClient
) {
    @GetMapping("/info/{laundryId}")
    fun getInfo(/*@AuthenticationPrincipal id: Int,*/@PathVariable("laundryId") laundryId: Int): ResponseResult {
        var id = 1
        return orderService.getInfo(id, laundryId)
    }

//    @PostMapping
//    fun createOrder(/*@AuthenticationPrincipal id: Int,*/ @RequestBody orderReqDTO: OrderDto.OrderReq): ResponseEntity<*> {
//        var id = 1
//        return orderService.createOrder(id, orderReqDTO)
//    }

//    @GetMapping
//    fun getOrders(/*@AuthenticationPrincipal id: Int,*/): ResponseEntity<*> {
//        var id = 1
//        return orderService.getOrders(id)
//    }

    @GetMapping("/{pickupId}")
    fun getOrderDetail( /*@AuthenticationPrincipal id: Int,*/ @PathVariable("pickupId") pickupId: Int): ResponseEntity<*> {
        var id = 1
        return orderService.getOrdersDetail(id, pickupId)
    }

    @PostMapping("/cancel")
    fun cancelOrder(/*@AuthenticationPrincipal id: Int,*/ @RequestBody pickupId: Map<String, Int>): ResponseEntity<*> {
        var id = 1
        return orderService.cancelOrder(id, pickupId["pickupId"] ?: 0)
    }

    @PostMapping("/kakaopay/ready")
    fun kakaoPayReady( /*@AuthenticationPrincipal id: Int,*/ @RequestBody kakaoPayReqDTO: KakaoPayDto.ReqDto): ResponseResult {
        var id = 1
        return kakaoPayClient.payReady(id, kakaoPayReqDTO)
    }

    @PostMapping("/kakaopay/approve")
    fun kakaoPayApprove( /*@AuthenticationPrincipal id: Int,*/ @RequestBody request: Map<String, String> ): ResponseResult {
        var id = 1
        return kakaoPayClient.payCompleted(id, request.getValue("pg_token"))
    }
}