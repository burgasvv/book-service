package org.burgas.bookservice.kafka;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.identity.IdentityFullResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, IdentityFullResponse> kafkaTemplate;

    public void sendIdentityFullResponse(final IdentityFullResponse identityFullResponse) {
        this.kafkaTemplate.send("identity-kafka-topic", identityFullResponse);
    }
}
