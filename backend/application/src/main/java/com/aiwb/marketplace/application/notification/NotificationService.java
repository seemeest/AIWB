package com.aiwb.marketplace.application.notification;

import com.aiwb.marketplace.application.ports.EmailSender;
import com.aiwb.marketplace.application.ports.NotificationRepository;
import com.aiwb.marketplace.application.ports.UserRepository;
import com.aiwb.marketplace.domain.notification.Notification;
import com.aiwb.marketplace.domain.notification.NotificationChannel;
import com.aiwb.marketplace.domain.notification.NotificationType;
import com.aiwb.marketplace.domain.user.User;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final Clock clock;

    public NotificationService(NotificationRepository notificationRepository,
                               EmailSender emailSender,
                               UserRepository userRepository,
                               Clock clock) {
        this.notificationRepository = notificationRepository;
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public void notifyUser(UUID userId, NotificationType type, String title, String message, String link) {
        Instant now = clock.instant();
        Notification webNotification = Notification.create(UUID.randomUUID(), userId, NotificationChannel.WEB, type, title, message, link, now);
        notificationRepository.save(webNotification.markSent(now));

        Notification emailNotification = Notification.create(UUID.randomUUID(), userId, NotificationChannel.EMAIL, type, title, message, link, now);
        Notification emailResult = sendEmail(userId, title, message, emailNotification, now);
        notificationRepository.save(emailResult);
    }

    public List<Notification> getUserNotifications(UUID userId, int limit) {
        return notificationRepository.findByUserId(userId, limit);
    }

    public Notification markRead(UUID notificationId) {
        return notificationRepository.markRead(notificationId, clock.instant());
    }

    private Notification sendEmail(UUID userId, String subject, String body, Notification emailNotification, Instant now) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return emailNotification.markFailed(now);
        }
        boolean sent = emailSender.send(user.get().getEmail(), subject, body);
        return sent ? emailNotification.markSent(now) : emailNotification.markFailed(now);
    }
}
