package org.washcode.washpang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class WashPangApplication

fun main(args: Array<String>) {
    runApplication<WashPangApplication>(*args)
}
