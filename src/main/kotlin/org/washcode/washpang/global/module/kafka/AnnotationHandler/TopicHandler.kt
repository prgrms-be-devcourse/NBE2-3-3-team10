//package org.washcode.washpang.global.module.kafka.AnnotationHandler
//
//import org.aspectj.lang.JoinPoint
//import org.aspectj.lang.annotation.AfterReturning
//import org.aspectj.lang.annotation.Aspect
//import org.springframework.kafka.core.KafkaTemplate
//import org.springframework.stereotype.Component
//import org.washcode.washpang.global.annotation.ShopTopic
//import org.washcode.washpang.global.annotation.UserTopic
//
//@Aspect
//@Component
//class ShopTopicHandler (
//    private val kafkaTemplate: KafkaTemplate<String, String>
//){
//    @AfterReturning(pointcut = "@annotation(shopTopic)", returning = "returnValue")
//    fun sendToKafka(joinPoint: JoinPoint, shopTopic: ShopTopic, returnValue: Any) {
//        kafkaTemplate.send("shop_topic", returnValue.toString())
//    }
//}
//
//@Aspect
//@Component
//class UserTopicHandler (
//    private val kafkaTemplate: KafkaTemplate<String, String>
//){
//    @AfterReturning(pointcut = "@annotation(userTopic)", returning = "returnValue")
//    fun sendToKafka(joinPoint: JoinPoint, userTopic: UserTopic, returnValue: Any) {
//        kafkaTemplate.send("user_topic", returnValue.toString())
//    }
//}