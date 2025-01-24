package org.washcode.washpang.global.client.feign.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.washcode.washpang.global.client.Dto.KakaoDto

@FeignClient(name = "KakaoApiServer", url = "https://kapi.kakao.com", configuration = [FeignClientConfiguration::class])
interface KakaoApiServerClient {

    @PostMapping("/v2/user/me")
    fun getKakaoUserInfo(@RequestHeader(name = "Authorization") authorization: String): String
}