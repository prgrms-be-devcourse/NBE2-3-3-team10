package org.washcode.washpang.domain.order.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.order.dto.OrderDto
import org.washcode.washpang.domain.order.service.OrderService
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
    fun getInfo( @AuthenticationPrincipal id: Int, @PathVariable("laundryId") laundryId: Int): ResponseResult {
        return orderService.getInfo(id, laundryId)
    }

    @PostMapping
    fun createOrder(/*@AuthenticationPrincipal id: Int,*/ @RequestBody orderReqDTO: OrderDto.OrderReq): ResponseResult  {
        var id = 1
        return orderService.createOrder(id, orderReqDTO)
    }

    @GetMapping
    fun getOrders(/*@AuthenticationPrincipal id: Int,*/): ResponseResult  {
        var id = 1
        return orderService.getOrders(id)
    }

    @GetMapping("/{pickupId}")
    fun getOrderDetail( @AuthenticationPrincipal id: Int, @PathVariable("pickupId") pickupId: Int): ResponseResult  {
        return orderService.getOrdersDetail(id, pickupId)
    }

    @PostMapping("/cancel")
    fun cancelOrder(@AuthenticationPrincipal id: Int, @RequestBody pickupId: Map<String, Int>): ResponseResult {
        return orderService.cancelOrder(id, pickupId["pickupId"] ?: 0)
    }

    @PostMapping("/kakaopay/ready")
    fun kakaoPayReady( @AuthenticationPrincipal id: Int, @RequestBody kakaoPayReqDTO: KakaoPayDto.ReqDto, response: HttpServletResponse): ResponseResult {
        return kakaoPayClient.payReady(id, kakaoPayReqDTO, response)
    }

    @PostMapping("/kakaopay/approve")
    fun kakaoPayApprove( @AuthenticationPrincipal id: Int, @RequestBody request: Map<String, String> ): ResponseResult {
        return kakaoPayClient.payCompleted(id, request.getValue("token"))
    }
}