package org.washcode.washpang.global.domain.kakaopay.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.washcode.washpang.global.domain.kakao.dto.KakaoDto
import org.washcode.washpang.global.domain.kakaopay.dto.KakaoPayDto
import org.washcode.washpang.global.module.feign.config.KakaoPayHeaderConfiguration

// https://open-api.kakaopay.com/online/v1/payment
@FeignClient(name = "KakaoPayServer", url = "https://open-api.kakaopay.com/online/v1/payment", configuration = [KakaoPayHeaderConfiguration::class])
interface KakaoPayApiClient {
    @PostMapping(value = ["/ready"], produces = ["application/json"])
    fun ready(@RequestHeader(name = "Authorization") authorization: String, @RequestBody dto: KakaoPayDto.ReadyReq): KakaoPayDto.ReadyRes?

//    @PostMapping("/approve")
//    fun approve(@RequestBody dto: String): KakaoPayDto.ReadyRes
}