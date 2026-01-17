package com.aiwb.marketplace.infrastructure.kafka;

import com.aiwb.marketplace.application.events.LoginAuditEvent;
import com.aiwb.marketplace.application.ports.LoginAuditRepository;
import com.aiwb.marketplace.domain.user.LoginAudit;
import com.aiwb.marketplace.infrastructure.geoip.GeoIpLocation;
import com.aiwb.marketplace.infrastructure.geoip.GeoIpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginAuditGeoIpListener {
    private static final Logger log = LoggerFactory.getLogger(LoginAuditGeoIpListener.class);
    private final GeoIpService geoIpService;
    private final LoginAuditRepository loginAuditRepository;

    public LoginAuditGeoIpListener(GeoIpService geoIpService,
                                   LoginAuditRepository loginAuditRepository) {
        this.geoIpService = geoIpService;
        this.loginAuditRepository = loginAuditRepository;
    }

    @KafkaListener(topics = "${app.kafka.topics.login-audit:login-audit}", groupId = "geoip-enricher")
    public void handle(LoginAuditEvent event) {
        if (event == null || event.userId() == null) {
            return;
        }
        Optional<GeoIpLocation> location = geoIpService.resolve(event.ip());
        if (location.isEmpty()) {
            return;
        }
        loginAuditRepository.findByUserId(event.userId()).ifPresent(existing -> {
            GeoIpLocation resolved = location.get();
            LoginAudit updated = existing.withLocation(resolved.country(), resolved.region(), resolved.city());
            loginAuditRepository.save(updated);
            log.info("GeoIP enriched login audit for user {}", event.userId());
        });
    }
}
