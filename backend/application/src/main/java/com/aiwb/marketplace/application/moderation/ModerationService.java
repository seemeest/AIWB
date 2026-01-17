package com.aiwb.marketplace.application.moderation;

import com.aiwb.marketplace.application.ports.AppealRepository;
import com.aiwb.marketplace.application.ports.BlockRepository;
import com.aiwb.marketplace.application.ports.BlockQueryRepository;
import com.aiwb.marketplace.application.ports.ComplaintRepository;
import com.aiwb.marketplace.application.ports.ModerationActionRepository;
import com.aiwb.marketplace.domain.moderation.Appeal;
import com.aiwb.marketplace.domain.moderation.AppealStatus;
import com.aiwb.marketplace.domain.moderation.Block;
import com.aiwb.marketplace.domain.moderation.Complaint;
import com.aiwb.marketplace.domain.moderation.ComplaintStatus;
import com.aiwb.marketplace.domain.moderation.ModerationAction;
import com.aiwb.marketplace.domain.moderation.ModerationTargetType;
import com.aiwb.marketplace.application.notification.NotificationService;
import com.aiwb.marketplace.domain.notification.NotificationType;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class ModerationService {
    private final ComplaintRepository complaintRepository;
    private final ModerationActionRepository actionRepository;
    private final BlockRepository blockRepository;
    private final BlockQueryRepository blockQueryRepository;
    private final AppealRepository appealRepository;
    private final Clock clock;
    private final Duration appealWindow;
    private final NotificationService notificationService;

    public ModerationService(ComplaintRepository complaintRepository,
                             ModerationActionRepository actionRepository,
                             BlockRepository blockRepository,
                             BlockQueryRepository blockQueryRepository,
                             AppealRepository appealRepository,
                             Clock clock,
                             Duration appealWindow,
                             NotificationService notificationService) {
        this.complaintRepository = complaintRepository;
        this.actionRepository = actionRepository;
        this.blockRepository = blockRepository;
        this.blockQueryRepository = blockQueryRepository;
        this.appealRepository = appealRepository;
        this.clock = clock;
        this.appealWindow = appealWindow;
        this.notificationService = notificationService;
    }

    public Complaint createComplaint(CreateComplaintCommand command) {
        Complaint complaint = Complaint.create(UUID.randomUUID(), command.authorId(), command.productId(), command.reason(), clock.instant());
        return complaintRepository.save(complaint);
    }

    public ModerationAction decide(ModerationDecisionCommand command) {
        ModerationAction action = ModerationAction.create(
                UUID.randomUUID(),
                command.moderatorId(),
                command.targetType(),
                command.targetId(),
                command.action(),
                command.reason(),
                clock.instant()
        );
        actionRepository.save(action);

        if (command.action().toLowerCase().contains("block")) {
            Block block = Block.create(
                    UUID.randomUUID(),
                    command.targetType(),
                    command.targetId(),
                    command.reason(),
                    clock.instant(),
                    command.blockedUntil()
            );
            blockRepository.save(block);
            if (command.targetType() == ModerationTargetType.SELLER || command.targetType() == ModerationTargetType.BUYER) {
                notificationService.notifyUser(command.targetId(), NotificationType.MODERATION_BLOCKED,
                        "Блокировка",
                        "Аккаунт заблокирован. Причина: " + command.reason(),
                        null);
            }
        }
        return action;
    }

    public Appeal createAppeal(CreateAppealCommand command) {
        Instant now = clock.instant();
        Instant blockCreatedAt = blockQueryRepository.findCreatedAt(command.blockId())
                .orElseThrow(() -> new ModerationException(ModerationError.APPEAL_NOT_FOUND, "Block not found"));
        if (now.isAfter(blockCreatedAt.plus(appealWindow))) {
            throw new ModerationException(ModerationError.APPEAL_WINDOW_EXPIRED, "Appeal window expired");
        }
        Appeal appeal = Appeal.create(UUID.randomUUID(), command.blockId(), command.authorId(), command.reason(), now);
        return appealRepository.save(appeal);
    }

    public Appeal decideAppeal(UUID appealId, boolean approved) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new ModerationException(ModerationError.APPEAL_NOT_FOUND, "Appeal not found"));
        AppealStatus status = approved ? AppealStatus.APPROVED : AppealStatus.REJECTED;
        Appeal updated = appeal.updateStatus(status);
        Appeal saved = appealRepository.save(updated);
        notificationService.notifyUser(saved.getAuthorId(), NotificationType.APPEAL_DECISION,
                "Решение по апелляции",
                "Апелляция " + saved.getId() + " рассмотрена: " + saved.getStatus().name(),
                null);
        return saved;
    }
}
