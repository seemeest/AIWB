package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.moderation.ModerationAction;

public interface ModerationActionRepository {
    ModerationAction save(ModerationAction action);
}
