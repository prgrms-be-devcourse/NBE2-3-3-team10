package org.washcode.washpang.global.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.databind.ObjectMapper

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper { return jacksonObjectMapper() }
}