package com.aiwb.marketplace.application.moderation;

import java.util.UUID;

public record CreateAppealCommand(UUID blockId, UUID authorId, String reason) {
}
