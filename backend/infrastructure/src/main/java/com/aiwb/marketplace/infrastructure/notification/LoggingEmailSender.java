package com.aiwb.marketplace.infrastructure.notification;

import com.aiwb.marketplace.application.ports.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingEmailSender implements EmailSender {
    private static final Logger log = LoggerFactory.getLogger(LoggingEmailSender.class);

    @Override
    public boolean send(String email, String subject, String body) {
        log.info("Email notification -> to={}, subject={}, body={}", email, subject, body);
        return true;
    }
}
