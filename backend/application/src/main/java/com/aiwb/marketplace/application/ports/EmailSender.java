package com.aiwb.marketplace.application.ports;

public interface EmailSender {
    boolean send(String email, String subject, String body);
}
