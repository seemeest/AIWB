package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.moderation.Block;

public interface BlockRepository {
    Block save(Block block);
}
