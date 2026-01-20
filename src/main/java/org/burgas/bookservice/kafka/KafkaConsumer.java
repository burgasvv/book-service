package org.burgas.bookservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.burgas.bookservice.dto.identity.IdentityFullResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(groupId = "consumer-config-group-id", topics = "identity-kafka-topic")
    public void kafkaListenerIdentityFullResponse(final ConsumerRecord<String, IdentityFullResponse> consumerRecord) {
        System.out.println(consumerRecord.value());
    }
}
