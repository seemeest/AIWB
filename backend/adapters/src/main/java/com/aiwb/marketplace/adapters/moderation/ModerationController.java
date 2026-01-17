package com.aiwb.marketplace.adapters.moderation;

import com.aiwb.marketplace.application.moderation.CreateAppealCommand;
import com.aiwb.marketplace.application.moderation.CreateComplaintCommand;
import com.aiwb.marketplace.application.moderation.ModerationDecisionCommand;
import com.aiwb.marketplace.application.moderation.ModerationService;
import com.aiwb.marketplace.domain.moderation.Appeal;
import com.aiwb.marketplace.domain.moderation.Complaint;
import com.aiwb.marketplace.domain.moderation.ModerationAction;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {
    private final ModerationService moderationService;

    public ModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PostMapping("/complaints")
    public ResponseEntity<ComplaintResponse> createComplaint(@Valid @RequestBody ComplaintRequest request) {
        Complaint complaint = moderationService.createComplaint(
                new CreateComplaintCommand(request.authorId(), request.productId(), request.reason())
        );
        return ResponseEntity.ok(new ComplaintResponse(
                complaint.getId(),
                complaint.getAuthorId(),
                complaint.getProductId(),
                complaint.getStatus().name(),
                complaint.getReason()
        ));
    }

    @PostMapping("/decisions")
    public ResponseEntity<DecisionResponse> decide(@Valid @RequestBody DecisionRequest request) {
        ModerationAction action = moderationService.decide(
                new ModerationDecisionCommand(
                        request.moderatorId(),
                        request.targetType(),
                        request.targetId(),
                        request.action(),
                        request.reason(),
                        request.blockedUntil()
                )
        );
        return ResponseEntity.ok(new DecisionResponse(
                action.getId(),
                action.getTargetType().name(),
                action.getTargetId(),
                action.getAction(),
                action.getReason()
        ));
    }

    @PostMapping("/appeals")
    public ResponseEntity<AppealResponse> appeal(@Valid @RequestBody AppealRequest request) {
        Appeal appeal = moderationService.createAppeal(
                new CreateAppealCommand(request.blockId(), request.authorId(), request.reason())
        );
        return ResponseEntity.ok(new AppealResponse(
                appeal.getId(),
                appeal.getBlockId(),
                appeal.getAuthorId(),
                appeal.getStatus().name(),
                appeal.getReason()
        ));
    }

    @PostMapping("/appeals/{id}")
    public ResponseEntity<AppealResponse> decideAppeal(@PathVariable("id") UUID appealId,
                                                       @RequestParam("approved") boolean approved) {
        Appeal appeal = moderationService.decideAppeal(appealId, approved);
        return ResponseEntity.ok(new AppealResponse(
                appeal.getId(),
                appeal.getBlockId(),
                appeal.getAuthorId(),
                appeal.getStatus().name(),
                appeal.getReason()
        ));
    }
}
