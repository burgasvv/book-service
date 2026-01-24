package org.burgas.bookservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.burgas.bookservice.dto.identity.IdentityFullResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public Map<String, Object> consumerConfig() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092",
                ConsumerConfig.GROUP_ID_CONFIG, "consumer-config-group-id",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class,
                JacksonJsonDeserializer.TYPE_MAPPINGS,
                "org.burgas.bookservice.dto.identity.IdentityFullResponse:org.burgas.bookservice.dto.identity.IdentityFullResponse"
        );
    }

    @Bean
    public ConsumerFactory<String, IdentityFullResponse> identityConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(this.consumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IdentityFullResponse> identityKafkaListener() {
        ConcurrentKafkaListenerContainerFactory<String, IdentityFullResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.identityConsumerFactory());
        return factory;
    }
}
