package org.washcode.washpang.global.annotation

import org.springframework.stereotype.Component

@Component
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserTopic()
