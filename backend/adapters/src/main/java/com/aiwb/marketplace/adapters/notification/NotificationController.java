package com.aiwb.marketplace.adapters.notification;

import com.aiwb.marketplace.application.notification.NotificationService;
import com.aiwb.marketplace.domain.notification.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> list(@RequestParam("userId") UUID userId,
                                                           @RequestParam(value = "limit", defaultValue = "20") int limit) {
        List<NotificationResponse> responses = notificationService.getUserNotifications(userId, limit).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable("id") UUID id) {
        Notification notification = notificationService.markRead(id);
        return ResponseEntity.ok(toResponse(notification));
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getChannel().name(),
                notification.getType().name(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getLink(),
                notification.getStatus().name(),
                notification.getCreatedAt(),
                notification.getSentAt(),
                notification.getReadAt()
        );
    }
}
