package org.washcode.washpang.global.module.feign.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.washcode.washpang.global.comm.client.Dto.KakaoDto
import org.washcode.washpang.global.module.feign.config.HeaderConfiguration

@FeignClient(name = "KakaoAuthServer", url = "https://kauth.kakao.com", configuration = [HeaderConfiguration::class])
interface KakaoAuthServerClient {

    @PostMapping("/oauth/token")
    fun getAccessToken(@RequestBody accessTokenDto: String): KakaoDto.KakaoAccessToken
}