package org.washcode.washpang.global.module.feign.config

import feign.Logger
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Bean

class KakaoPayHeaderConfiguration {
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { requestTemplate: RequestTemplate ->
            requestTemplate.header("Content-Type", "application/json")
        }
    }

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL;
    }
}