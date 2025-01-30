package org.washcode.washpang.global.module.feign.config

import feign.*
import org.springframework.context.annotation.Bean

class KakaoHeaderConfiguration {

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { requestTemplate: RequestTemplate ->
            // 헤더에 Content-Type을 추가
            requestTemplate.header("Content-Type", "application/x-www-form-urlencoded")
        }
    }

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL;
    }
}