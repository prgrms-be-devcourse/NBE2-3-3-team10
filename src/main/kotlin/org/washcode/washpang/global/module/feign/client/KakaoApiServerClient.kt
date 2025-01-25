package org.washcode.washpang.global.module.feign.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "KakaoApiServer", url = "https://kapi.kakao.com", configuration = [FeignClientConfiguration::class])
interface KakaoApiServerClient {

    @PostMapping("/v2/user/me")
    fun getKakaoUserInfo(@RequestHeader(name = "Authorization") authorization: String): String
}