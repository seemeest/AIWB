package com.aiwb.marketplace.infrastructure.kafka;

import com.aiwb.marketplace.application.events.LoginAuditEvent;
import com.aiwb.marketplace.application.ports.LoginAuditEventPublisher;
import com.aiwb.marketplace.infrastructure.config.KafkaTopicsProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaLoginAuditEventPublisher implements LoginAuditEventPublisher {
    private final KafkaTemplate<String, LoginAuditEvent> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    public KafkaLoginAuditEventPublisher(KafkaTemplate<String, LoginAuditEvent> kafkaTemplate,
                                         KafkaTopicsProperties topics) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }

    @Override
    public void publish(LoginAuditEvent event) {
        kafkaTemplate.send(topics.getLoginAudit(), event.userId().toString(), event);
    }
}
