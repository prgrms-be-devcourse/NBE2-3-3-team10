package org.washcode.washpang.global.domain.kakao.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.washcode.washpang.global.domain.kakao.dto.KakaoDto
import org.washcode.washpang.global.module.feign.config.KakaoHeaderConfiguration

@FeignClient(name = "KakaoAuthServer", url = "https://kauth.kakao.com", configuration = [KakaoHeaderConfiguration::class])
interface KakaoAuthServerClient {

    @PostMapping("/oauth/token")
    fun getAccessToken(@RequestBody accessTokenDto: String): KakaoDto.AccessRes
}