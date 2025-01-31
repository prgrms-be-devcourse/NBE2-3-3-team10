//package org.washcode.washpang.global.config
//
//import org.apache.kafka.common.serialization.Serdes
//import org.apache.kafka.streams.StreamsBuilder
//import org.apache.kafka.streams.StreamsConfig
//import org.apache.kafka.streams.kstream.Consumed
//import org.apache.kafka.streams.kstream.KStream
//import org.apache.kafka.streams.kstream.Produced
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.annotation.EnableKafkaStreams
//import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
//import org.springframework.kafka.config.StreamsBuilderFactoryBean
//import org.springframework.kafka.config.KafkaStreamsConfiguration
//
//@Configuration
//@EnableKafkaStreams
//class KafkaConfig (
//    @Value("\${spring.kafka.bootstrap-servers}")
//    private val bootstrapServers: String,
//
//    @Value("\${spring.kafka.streams.application-id}")
//    private val applicationId: String
//){
//    @Bean
//    fun kStreamsConfigs(): KafkaStreamsDefaultConfiguration {
//        val props: MutableMap<String, Any?> = HashMap()
//        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
//        props[StreamsConfig.APPLICATION_ID_CONFIG] = applicationId
//        props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.StringSerde::class.java
//        props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.StringSerde::class.java
//
//        return KafkaStreamsDefaultConfiguration()
//    }
//
//    @Bean
//    @Throws(Exception::class)
//    fun shopKStream(factoryBean: StreamsBuilderFactoryBean): KStream<String, String> {
//        val builder: StreamsBuilder = factoryBean.getObject()
//
//        val shopSourceStream: KStream<String, String> = builder.stream(
//            "shop_topic",
//            Consumed.with(Serdes.String(), Serdes.String())
//        )
//
//        val shopTransformedStream: KStream<String, String> = shopSourceStream
//            .mapValues { value -> "shop click : $value" }
//
//        shopTransformedStream.to(
//            "shop_topic_processed",
//            Produced.with(Serdes.String(), Serdes.String())
//        )
//
//        return shopSourceStream
//    }
//
//    @Bean
//    @Throws(Exception::class)
//    fun userKStream(factoryBean: StreamsBuilderFactoryBean): KStream<String, String> {
//        val builder: StreamsBuilder = factoryBean.getObject()
//
//        val userSourceStream: KStream<String, String> = builder.stream(
//            "user_topic",
//            Consumed.with(Serdes.String(), Serdes.String())
//        )
//
//        val userTransformedStream: KStream<String, String> = userSourceStream
//            .mapValues { value -> "user click : $value" }
//
//        userTransformedStream.to(
//            "user_topic_processed",
//            Produced.with(Serdes.String(), Serdes.String())
//        )
//
//        return userSourceStream
//    }
//}
