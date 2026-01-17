package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.application.events.LoginAuditEvent;

public interface LoginAuditEventPublisher {
    void publish(LoginAuditEvent event);
}
